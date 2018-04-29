package Service;

import Domain.Nota;
import Domain.Student;
import Utils.Utils;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private StudentService studentService;
    private TemaService temaService;
    private NotaService notaService;

    int grupa = 0;
    String baseUri = Utils.reportHtmlPath;
    String beginHtml = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <title>Raport</title>\n" +
            "    <link href=\"reportStylesheet.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h1>Raport</h1>\n" +
            "    <hr>";
    String endHtml = "</body>\n" +
            "</html>";
    String beginCard = "<table class = \"Card\">\n" +
            "        <tr>";
    String endCard = "</tr>\n" +
            "    </table>\n" +
            "    <br>";
    String beginTableData = "<td><table class = \"DataTable\">" +
            "<tr><th>Nume</th><th>Grupa</th><th>Media</th></tr>";
    String endTableData = "</table></td>";

    public ReportService(StudentService studentService, TemaService temaService, NotaService notaService) {
        this.studentService = studentService;
        this.temaService = temaService;
        this.notaService = notaService;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public TemaService getTemaService() {
        return temaService;
    }

    public NotaService getNotaService() {
        return notaService;
    }

    private void saveGraficNote() throws IOException {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> chart = new BarChart<>(xAxis,yAxis);

        xAxis.setLabel("Note");
        yAxis.setLabel("Valoare");

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<Nota> all;
        if (grupa == 0)
            all = notaService.getAll();
        else
            all = Filter.filterAndSorter(notaService.getAll(), (nota) -> notaService.findStudent(nota).get().getGrupa() == grupa, null);

        Map<Integer, Integer> note = new HashMap<>();
        for (Nota nota : all) {
            Double n = nota.getNota();
            int nf = ((int) Math.floor(n));
            if (nf < 5) {
                note.putIfAbsent(1, 0);
                note.put(1, note.get(1) + 1);
            } else {
                note.putIfAbsent(nf, 0);
                note.put(nf, note.get(nf) + 1);
            }
        }

        for (Integer nota : note.keySet()) {
            if (nota == 1)
                series.getData().add(new XYChart.Data<>("1-5", note.get(nota)));
            else if (nota == 10)
                series.getData().add(new XYChart.Data<>("10", note.get(nota)));
            else
                series.getData().add(new XYChart.Data<>(nota.toString() + "-" + (nota+1), note.get(nota)));
        }

        chart.setLegendVisible(false);
        chart.getData().add(series);
        chart.setPrefWidth(1000);
        chart.setPrefHeight(700);
        chart.setStyle("-fx-font-size: 30px;");

        Scene scene = new Scene(chart);

        WritableImage snapshot = scene.snapshot(null);
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File(baseUri + "/graficNote.png"));
    }

    private void saveGraficGrupe() throws IOException {
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        final BarChart<Number, String> chart = new BarChart<>(xAxis,yAxis);

        xAxis.setLabel("Valoare");
        yAxis.setLabel("Medie");

        XYChart.Series<Number, String> series = new XYChart.Series<>();

        studentService.getGrupe().forEach(grupa -> {
            series.getData().add(new XYChart.Data<>(notaService.getMedia(grupa), grupa.toString()));
        });

        chart.setLegendVisible(false);
        chart.getData().add(series);
        chart.setPrefWidth(1000);
        chart.setPrefHeight(700);
        chart.setStyle("-fx-font-size: 30px;");

        Scene scene = new Scene(chart);

        WritableImage snapshot = scene.snapshot(null);
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File(baseUri + "/graficGrupe.png"));
    }

    private String generateDataColumn(String... text) {
        String dataCol = "<td class=\"DataColumn LeftColumn\">";

        for (String t : text) {
            dataCol += "<p>" + t + "</p>";
        }

        dataCol += "</td>";
        return dataCol;
    }

    private String generateImage(String imagePath) {
        return "<td>\n" +
                "<img src = \"" + imagePath + "\"/>\n" +
                "</td>";
    }

    private String generateTableRow(String... text) {
        String dataCol = "<tr>";

        for (String t : text) {
            dataCol += "<td>" + t + "</td>";
        }

        dataCol += "</tr>";
        return dataCol;
    }

    private String constructReport(boolean mediaStudenti, boolean studentiNuExamen, boolean graficNote, boolean graficGrupe) throws IOException {
        String html = beginHtml;

        List<Student> all;
        if (grupa == 0)
            all = studentService.getAll();
        else
            all = studentService.filtreazaStudentiKeyword("grupa: " + grupa, studentService.getAll());

        if (mediaStudenti) {
            html += beginCard;
            html += generateDataColumn("Media", "studentilor", "la", "laborator");

            html += beginTableData;
            for (Student student : all) {
                html += generateTableRow(student.getNume(), String.valueOf(student.getGrupa()), String.format("%.2f", notaService.getMedia(student.getNume())));
            }
            html += endTableData;

            html += endCard;
        }

        if (studentiNuExamen) {
            html += beginCard;
            html += generateDataColumn("Studentii", "care nu", "pot intra", "in examen");

            html += beginTableData;
            for (Student student : all) {
                Double media = notaService.getMedia(student.getNume());
                if (media < 5)
                    html += generateTableRow(student.getNume(), String.valueOf(student.getGrupa()), String.format("%.2f", media));
            }
            html += endTableData;

            html += endCard;
        }

        if (graficNote) {
            html += beginCard;
            html += generateDataColumn("Graficul", "notelor", "pe", "grupa");
            html += generateImage("graficNote.png");
            html += endCard;
            saveGraficNote();
        }

        if (graficGrupe) {
            html += beginCard;
            html += generateDataColumn("Graficul", "mediilor", "grupelor");
            html += generateImage("graficGrupe.png");
            html += endCard;
            saveGraficGrupe();
        }

        html += endHtml;
        return html;
    }

    public void generateReport(File file, String grupa, boolean mediaStudenti, boolean studentiNuExamen, boolean graficNote, boolean graficGrupe) throws Exception {
        if (! grupa.equals("Toate grupele"))
            this.grupa = Integer.parseInt(grupa);
        else
            this.grupa = 0;

        String html = constructReport(mediaStudenti, studentiNuExamen, graficNote, graficGrupe);

        ConverterProperties properties = new ConverterProperties();
        properties.setBaseUri(baseUri);
        properties.setFontProvider(new DefaultFontProvider(true, true, true));
        HtmlConverter.convertToPdf(html, new FileOutputStream(file), properties);

        System.out.println("PDF Created!");
    }
}

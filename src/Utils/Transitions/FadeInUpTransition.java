package Utils.Transitions;

import Utils.Utils;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

public class FadeInUpTransition {
    protected static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25D, 0.1D, 0.25D, 1.0D);

    public FadeInUpTransition(Node node) {
        playTransition(node, Duration.millis(500));
    }

    public FadeInUpTransition(Node node, Duration duration) {
        playTransition(node, duration);
    }

    private void playTransition(Node node, Duration duration) {
        node.setCache(true);
        node.setCacheHint(CacheHint.SPEED);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(1);

        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(30);
        tt.setToY(0);
        tt.setCycleCount(1);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(ft, tt);
        parallelTransition.setCycleCount(1);
        parallelTransition.setInterpolator(WEB_EASE);

        parallelTransition.play();
    }
}

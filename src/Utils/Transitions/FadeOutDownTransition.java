package Utils.Transitions;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.function.Predicate;

public class FadeOutDownTransition {
    protected static final Interpolator WEB_EASE = Interpolator.SPLINE(0.25D, 0.1D, 0.25D, 1.0D);
    private ParallelTransition parallelTransition;

    public FadeOutDownTransition(Node node) {
        playTransition(node, Duration.millis(500));
    }

    public FadeOutDownTransition(Node node, Duration duration) {
        playTransition(node, duration);
    }

    private void playTransition(Node node, Duration duration) {
        node.setCache(true);
        node.setCacheHint(CacheHint.SPEED);

        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(1);

        TranslateTransition tt = new TranslateTransition(duration, node);
        tt.setFromY(0);
        tt.setToY(30);
        tt.setCycleCount(1);

        parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(ft, tt);
        parallelTransition.setCycleCount(1);
        parallelTransition.setInterpolator(WEB_EASE);

        parallelTransition.play();
    }

    public void setOnFinished(EventHandler eventHandler) {
        parallelTransition.setOnFinished(eventHandler);
    }
}

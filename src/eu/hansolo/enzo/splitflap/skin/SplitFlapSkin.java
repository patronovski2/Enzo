package eu.hansolo.enzo.splitflap.skin;

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import eu.hansolo.enzo.splitflap.FlipEvent;
import eu.hansolo.enzo.splitflap.SplitFlap;
import eu.hansolo.enzo.splitflap.behavior.SplitFlapBehavior;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.InnerShadowBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.RotateBuilder;
import javafx.util.Duration;

import java.util.ArrayList;


public class SplitFlapSkin extends BehaviorSkinBase<SplitFlap, SplitFlapBehavior> {
    private static final FlipEvent FLIP_FINISHED = new FlipEvent(FlipEvent.FLIP_FINISHED);
    private static final double DEFAULT_WIDTH  = 112;
    private static final double DEFAULT_HEIGHT = 189;
    private static final double MINIMUM_WIDTH  = 5;
    private static final double MINIMUM_HEIGHT = 5;
    private static final double MAXIMUM_WIDTH  = 1024;
    private static final double MAXIMUM_HEIGHT = 1024;
    private static double       aspectRatio;
    private SplitFlap           control;
    private ArrayList<String>   selectedSet;
    private int                 currentSelectionIndex;
    private int                 nextSelectionIndex;
    private double              size;
    private double              width;
    private double              height;
    private Pane                pane;
    private Region              fixtureRightFrame;
    private Region              fixtureRightMain;
    private Region              fixtureRightFrameDark;
    private Region              fixtureRightMainDark;
    private Region              fixtureLeftFrame;
    private Region              fixtureLeftMain;
    private Region              fixtureLeftFrameDark;
    private Region              fixtureLeftMainDark;
    private InnerShadow         innerShadow;
    private InnerShadow         innerHighlight;
    private InnerShadow         reversedInnerShadow;
    private InnerShadow         reversedInnerHighlight;
    private Region              upperBackground;
    private Canvas              upperBackgroundText;
    private GraphicsContext     ctxUpperBackgroundText;
    private Region              lowerBackground;
    private Canvas              lowerBackgroundText;
    private GraphicsContext     ctxLowerBackgroundText;
    private Region              flap;
    private Canvas              flapTextFront;
    private GraphicsContext     ctxTextFront;
    private Canvas              flapTextBack;
    private GraphicsContext     ctxTextBack;
    private Font                font;
    private Rotate              rotateFlap;
    private Timeline            timeline;
    private KeyFrame            keyFrame;
    private KeyValue            keyValueFlap;



    // ******************** Constructors **************************************
    public SplitFlapSkin(final SplitFlap CONTROL) {
        super(CONTROL, new SplitFlapBehavior(CONTROL));
        control               = CONTROL;
        selectedSet           = control.getSelectedSet();
        currentSelectionIndex = control.getSelectedSet().indexOf(control.getText());
        nextSelectionIndex    = currentSelectionIndex + 1 > control.getSelectedSet().size() ? 0 : currentSelectionIndex + 1;
        aspectRatio           = DEFAULT_HEIGHT / DEFAULT_WIDTH;
        pane                  = new Pane();
        rotateFlap            = RotateBuilder.create().axis(Rotate.X_AXIS).angle(0).build();
        timeline              = new Timeline();
        init();
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(control.getPrefWidth(), 0.0) <= 0 || Double.compare(control.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(control.getWidth(), 0.0) <= 0 || Double.compare(control.getHeight(), 0.0) <= 0) {
            if (control.getPrefWidth() > 0 && control.getPrefHeight() > 0) {
                control.setPrefSize(control.getPrefWidth(), control.getPrefHeight());
            } else {
                control.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            }
        }

        if (Double.compare(control.getMinWidth(), 0.0) <= 0 || Double.compare(control.getMinHeight(), 0.0) <= 0) {
            control.setMinSize(MINIMUM_WIDTH, MINIMUM_HEIGHT);
        }

        if (Double.compare(control.getMaxWidth(), 0.0) <= 0 || Double.compare(control.getMaxHeight(), 0.0) <= 0) {
            control.setMaxSize(MAXIMUM_WIDTH, MAXIMUM_HEIGHT);
        }

        if (control.getPrefWidth() != DEFAULT_WIDTH || control.getPrefHeight() != DEFAULT_HEIGHT) {
            aspectRatio = control.getPrefHeight() / control.getPrefWidth();
        }

        selectedSet.addAll(control.getSelectedSet());
    }

    private void initGraphics() {

        fixtureRightFrame = new Region();
        fixtureRightFrame.getStyleClass().setAll("fixture-right-frame");


        fixtureRightMain = new Region();
        fixtureRightMain.getStyleClass().setAll("fixture-right-main");


        fixtureRightFrameDark = new Region();
        fixtureRightFrameDark.getStyleClass().setAll("fixture-right-frame-dark");


        fixtureRightMainDark = new Region();
        fixtureRightMainDark.getStyleClass().setAll("fixture-right-main-dark");


        fixtureLeftFrame = new Region();
        fixtureLeftFrame.getStyleClass().setAll("fixture-left-frame");


        fixtureLeftMain = new Region();
        fixtureLeftMain.getStyleClass().setAll("fixture-left-main");


        fixtureLeftFrameDark = new Region();
        fixtureLeftFrameDark.getStyleClass().setAll("fixture-left-frame-dark");


        fixtureLeftMainDark = new Region();
        fixtureLeftMainDark.getStyleClass().setAll("fixture-left-main-dark");

        innerShadow = InnerShadowBuilder.create()
                                        .radius(3.0 / 112.0 * DEFAULT_WIDTH)
                                        .color(Color.BLACK)
                                        .blurType(BlurType.GAUSSIAN)
                                        .build();
        innerHighlight = InnerShadowBuilder.create()
                                           .offsetY(1.0)
                                           .radius(2.0 / 112.0 * DEFAULT_WIDTH)
                                           .color(Color.WHITE)
                                           .blurType(BlurType.GAUSSIAN)
                                           .input(innerShadow)
                                           .build();

        reversedInnerShadow = InnerShadowBuilder.create()
                                                .radius(3.0 / 112.0 * DEFAULT_WIDTH)
                                                .color(Color.BLACK)
                                                .blurType(BlurType.GAUSSIAN)
                                                .build();
        reversedInnerHighlight = InnerShadowBuilder.create()
                                                   .offsetY(-1.0)
                                                   .radius(2.0 / 112.0 * DEFAULT_WIDTH)
                                                   .color(Color.WHITE)
                                                   .blurType(BlurType.GAUSSIAN)
                                                   .input(innerShadow)
                                                   .build();

        upperBackground = new Region();
        upperBackground.getStyleClass().setAll("flap-upper");
        upperBackground.setEffect(innerHighlight);

        //font = Font.font("Bebas Neue", DEFAULT_HEIGHT);
        font = Font.loadFont(getClass().getResourceAsStream("/resources/bebasneue.otf"), DEFAULT_HEIGHT);

        upperBackgroundText    = new Canvas();
        ctxUpperBackgroundText = upperBackgroundText.getGraphicsContext2D();
        ctxUpperBackgroundText.setTextBaseline(VPos.CENTER);
        ctxUpperBackgroundText.setTextAlign(TextAlignment.CENTER);

        lowerBackground = new Region();
        lowerBackground.getStyleClass().setAll("flap-lower");
        lowerBackground.setEffect(innerHighlight);

        lowerBackgroundText    = new Canvas();
        ctxLowerBackgroundText = lowerBackgroundText.getGraphicsContext2D();
        ctxLowerBackgroundText.setTextBaseline(VPos.CENTER);
        ctxLowerBackgroundText.setTextAlign(TextAlignment.CENTER);

        flap = new Region();
        flap.getStyleClass().setAll("flap-upper");
        flap.setEffect(innerHighlight);
        flap.getTransforms().add(rotateFlap);

        flapTextFront = new Canvas();
        ctxTextFront  = flapTextFront.getGraphicsContext2D();
        ctxTextFront.setTextBaseline(VPos.CENTER);
        ctxTextFront.setTextAlign(TextAlignment.CENTER);
        flapTextFront.getTransforms().add(rotateFlap);

        flapTextBack  = new Canvas();
        ctxTextBack   = flapTextBack.getGraphicsContext2D();
        ctxTextBack.setTextBaseline(VPos.CENTER);
        ctxTextBack.setTextAlign(TextAlignment.CENTER);
        flapTextBack.getTransforms().add(rotateFlap);
        flapTextBack.setVisible(false);

        pane.getChildren().setAll(fixtureRightFrame,
                                  fixtureRightMain,
                                  fixtureRightFrameDark,
                                  fixtureRightMainDark,
                                  fixtureLeftFrame,
                                  fixtureLeftMain,
                                  fixtureLeftFrameDark,
                                  fixtureLeftMainDark,
                                  upperBackground,
                                  lowerBackground,
                                  upperBackgroundText,
                                  lowerBackgroundText,
                                  flap,
                                  flapTextFront,
                                  flapTextBack);

        getChildren().setAll(pane);
        resize();
    }

    private void registerListeners() {
        registerChangeListener(control.widthProperty(), "RESIZE");
        registerChangeListener(control.heightProperty(), "RESIZE");
        registerChangeListener(control.prefWidthProperty(), "PREF_SIZE");
        registerChangeListener(control.prefHeightProperty(), "PREF_SIZE");
        registerChangeListener(control.textProperty(), "TEXT");
        registerChangeListener(control.characterSetProperty(), "CHARACTER_SET");

        control.getStyleClass().addListener(new ListChangeListener<String>() {
            @Override public void onChanged(Change<? extends String> change) {
                resize();
            }
        });

        rotateFlap.angleProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldAngle, Number newAngle) {
                if (newAngle.doubleValue() > 90) {
                    flapTextFront.setVisible(false);
                    flapTextBack.setVisible(true);
                    flap.setEffect(reversedInnerHighlight);
                }
                if (newAngle.doubleValue() < 90) {
                    // frontside text visible = true
                }
            }
        });

        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override public void handle(final ActionEvent EVENT) {
                control.fireEvent(FLIP_FINISHED);
                if (Double.compare(rotateFlap.getAngle(), 180) == 0) {
                    flap.setEffect(innerHighlight);
                    rotateFlap.setAngle(0);
                    flapTextBack.setVisible(false);
                    flapTextFront.setVisible(true);
                    refreshTextCtx();
                    if (control.getText() != selectedSet.get(currentSelectionIndex)) {
                        flipForward();
                    }
                } else if(Double.compare(rotateFlap.getAngle(), 0) == 0) {
                    rotateFlap.setAngle(180);
                }
            }
        });
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("PREF_SIZE".equals(PROPERTY)) {
            aspectRatio = control.getPrefHeight() / control.getPrefWidth();
        } else if ("TEXT".equals(PROPERTY)) {
            flipForward();
        } else if ("CHARACTER_SET".equals(PROPERTY)) {
            selectedSet.clear();
            for (String text : control.getSelectedSet()) {
                selectedSet.add(text);
            }
        }
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_HEIGHT) {
        double prefHeight = DEFAULT_HEIGHT;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    @Override protected double computePrefHeight(final double PREF_WIDTH) {
        double prefWidth = DEFAULT_WIDTH;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - control.getInsets().getLeft() - control.getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computeMinWidth(final double MIN_HEIGHT) {
        return super.computeMinWidth(Math.max(MINIMUM_HEIGHT, MIN_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom()));
    }

    @Override protected double computeMinHeight(final double MIN_WIDTH) {
        return super.computeMinHeight(Math.max(MINIMUM_WIDTH, MIN_WIDTH - control.getInsets().getLeft() - control.getInsets().getRight()));
    }

    @Override protected double computeMaxWidth(final double MAX_HEIGHT) {
        return super.computeMaxWidth(Math.min(MAXIMUM_HEIGHT, MAX_HEIGHT - control.getInsets().getTop() - control.getInsets().getBottom()));
    }

    @Override protected double computeMaxHeight(final double MAX_WIDTH) {
        return super.computeMaxHeight(Math.min(MAXIMUM_WIDTH, MAX_WIDTH - control.getInsets().getLeft() - control.getInsets().getRight()));
    }

    public void flipForward() {
        timeline.stop();

        currentSelectionIndex++;
        if (currentSelectionIndex >= selectedSet.size()) {
            currentSelectionIndex = 0;
        }
        nextSelectionIndex = currentSelectionIndex + 1;
        if (nextSelectionIndex >= selectedSet.size()) {
            nextSelectionIndex = 0;
        }
        keyValueFlap = new KeyValue(rotateFlap.angleProperty(), 180, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        keyFrame     = new KeyFrame(Duration.millis(control.getFlipTime()), keyValueFlap);
        timeline.getKeyFrames().setAll(keyFrame);
        timeline.play();
    }

    public void flipBackward() {
        timeline.stop();
        keyValueFlap = new KeyValue(rotateFlap.angleProperty(), -180, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        keyFrame     = new KeyFrame(Duration.millis(control.getFlipTime()), keyValueFlap);
        timeline.getKeyFrames().setAll(keyFrame);
        timeline.play();
    }

    private void refreshTextCtx() {
        double flapWidth  = flapTextFront.getWidth();
        double flapHeight = flapTextFront.getHeight();

        // set the text on the upper background
        ctxUpperBackgroundText.clearRect(0, 0, flapWidth, flapHeight);
        ctxUpperBackgroundText.setFill(control.getTextColor());
        ctxUpperBackgroundText.fillText(selectedSet.get(nextSelectionIndex), width * 0.5, height * 0.55);

        // set the text on the lower background
        ctxLowerBackgroundText.clearRect(0, 0, flapWidth, flapHeight);
        ctxLowerBackgroundText.setFill(control.getTextColor());
        ctxLowerBackgroundText.fillText(selectedSet.get(currentSelectionIndex), width * 0.5, height * 0.041);

        // set the text on the flap front
        ctxTextFront.clearRect(0, 0, flapWidth, flapHeight);
        ctxTextFront.setFill(control.getTextColor());
        ctxTextFront.fillText(selectedSet.get(currentSelectionIndex), width * 0.5, height * 0.55);

        // set the text on the flap back
        ctxTextBack.clearRect(0, 0, flapWidth, flapHeight);
        ctxTextBack.setFill(control.getTextColor());
        ctxTextBack.save();
        ctxTextBack.scale(1,-1);
        ctxTextBack.fillText(selectedSet.get(nextSelectionIndex), width * 0.5, -height * 0.45);
        ctxTextBack.restore();
    }


    // ******************** Resizing ******************************************
    private void resize() {
        size   = control.getWidth() < control.getHeight() ? control.getWidth() : control.getHeight();
        width  = control.getWidth();
        height = control.getHeight();
        if (control.isKeepAspect()) {
            if (aspectRatio * width > height) {
                width  = 1 / (aspectRatio / height);
            } else if (1 / (aspectRatio / height) > width) {
                height = aspectRatio * width;
            }
        }

        fixtureRightFrame.setPrefSize(0.08035714285714286 * width, 0.164021164021164 * height);
        fixtureRightFrame.setTranslateX(0.9196428571428571 * width);
        fixtureRightFrame.setTranslateY(0.41798941798941797 * height);

        fixtureRightMain.setPrefSize(0.0625 * width, 0.15343915343915343 * height);
        fixtureRightMain.setTranslateX(0.9285714285714286 * width);
        fixtureRightMain.setTranslateY(0.42328042328042326 * height);

        fixtureRightFrameDark.setPrefSize(0.08035714285714286 * width, 0.164021164021164 * height);
        fixtureRightFrameDark.setTranslateX(0.9196428571428571 * width);
        fixtureRightFrameDark.setTranslateY(0.41798941798941797 * height);

        fixtureRightMainDark.setPrefSize(0.0625 * width, 0.15343915343915343 * height);
        fixtureRightMainDark.setTranslateX(0.9285714285714286 * width);
        fixtureRightMainDark.setTranslateY(0.42328042328042326 * height);

        fixtureLeftFrame.setPrefSize(0.08035714285714286 * width, 0.164021164021164 * height);
        fixtureLeftFrame.setTranslateY(0.41798941798941797 * height);

        fixtureLeftMain.setPrefSize(0.0625 * width, 0.15343915343915343 * height);
        fixtureLeftMain.setTranslateX(0.008928571428571428 * width);
        fixtureLeftMain.setTranslateY(0.42328042328042326 * height);

        fixtureLeftFrameDark.setPrefSize(0.08035714285714286 * width, 0.164021164021164 * height);
        fixtureLeftFrameDark.setTranslateY(0.41798941798941797 * height);

        fixtureLeftMainDark.setPrefSize(0.0625 * width, 0.15343915343915343 * height);
        fixtureLeftMainDark.setTranslateX(0.008928571428571428 * width);
        fixtureLeftMainDark.setTranslateY(0.42328042328042326 * height);

        upperBackground.setPrefSize(width, 0.49206349206349204 * height);
        lowerBackground.setPrefSize(width, 0.49206349206349204 * height);
        lowerBackground.setTranslateY(0.5079365079365079 * height);

        font = Font.font("Bebas Neue", height);

        upperBackgroundText.setWidth(width);
        upperBackgroundText.setHeight(0.49206349206349204 * height);
        lowerBackgroundText.setWidth(width);
        lowerBackgroundText.setHeight(0.49206349206349204 * height);
        lowerBackgroundText.setTranslateY(0.5079365079365079 * height);

        flap.setPrefSize(width, 0.49206349206349204 * height);
        rotateFlap.setPivotY(height * 0.5);

        flapTextFront.setWidth(width);
        flapTextFront.setHeight(0.49206349206349204 * height);
        flapTextBack.setWidth(width);
        flapTextBack.setHeight(0.49206349206349204 * height);

        ctxUpperBackgroundText.setFont(font);
        ctxLowerBackgroundText.setFont(font);
        ctxTextFront.setFont(font);
        ctxTextBack.setFont(font);

        refreshTextCtx();

        innerShadow.setRadius(3.0 / 112.0 * size);
        innerHighlight.setRadius(2.0 / 112.0 * size);

        reversedInnerShadow.setRadius(3.0 / 112 * size);
        reversedInnerHighlight.setRadius(2.0 / 112.0 * size);
    }

}
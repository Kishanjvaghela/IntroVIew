package com.introlayout;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.introlayout.animation.AnimationFactory;
import com.introlayout.animation.AnimationListener;
import com.introlayout.pref.PreferencesManager;
import com.introlayout.shape.Rect;
import com.introlayout.shape.Shape;
import com.introlayout.target.Target;
import com.introlayout.target.ViewTarget;
import com.introlayout.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.github.kshitij_jain.indicatorview.IndicatorView;

/**
 * Created by kisha_000 on 2/28/2017.
 */

public class IntroView extends RelativeLayout {

    /**
     * Mask color
     */
    private int maskColor;

    /**
     * MaterialIntroView will start
     * showing after delayMillis seconds
     * passed
     */
    private long delayMillis;

    /**
     * We don't draw MaterialIntroView
     * until isReady field set to true
     */
    private boolean isReady;


    /**
     * Animation duration
     */
    private long fadeAnimationDuration;

    /**
     * targetShape focus on target
     * and clear circle to focus
     */
    private Shape targetShape;


    /**
     * Target View
     */
    private List<Target> targetViewList = new ArrayList<>();


    /**
     * Handler will be used to
     * delay MaterialIntroView
     */
    private Handler handler;

    /**
     * All views will be drawn to
     * this bitmap and canvas then
     * bitmap will be drawn to canvas
     */
    private Bitmap bitmap;
    private Canvas canvas;

    /**
     * Circle padding
     */
    private int padding;

    /**
     * Layout width/height
     */
    private int width;
    private int height;


    /**
     * Info dialog view
     */
    private View infoView;

    /**
     * Info Dialog Text
     */
    private TextView textViewInfo;

    /**
     * arrow image view
     */
    private ImageView leftImageView, rightImageView;

    /**
     * Info dialog text color
     */
    private int colorTextViewInfo;

    /**
     * Save/Retrieve status of MaterialIntroView
     * If Intro is already learnt then don't show
     * it again.
     */
    private PreferencesManager preferencesManager;


    /**
     * When layout completed, we set this true
     * Otherwise onGlobalLayoutListener stuck on loop.
     */
    private boolean isLayoutCompleted;

    /**
     * Notify user when MaterialIntroView is dismissed
     */
    private MaterialIntroListener materialIntroListener;

    /**
     * Perform click operation to target
     * if this is true
     */
    private boolean isPerformClick;

    /**
     * Disallow this MaterialIntroView from showing up more than once at a time
     */
    private boolean isIdempotent;


    /**
     * Eraser
     */
    private Paint eraser;

    private ViewGroup parentView;

    private int currentViewPos = 0;

    private String introId;

    private IndicatorView indicatorView;

    private View bottomView;

    private boolean isNavigationButton;

    public IntroView(Context context) {
        super(context);
        init(context);
    }

    public IntroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IntroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);

        /**
         * set default values
         */
        maskColor = Constants.DEFAULT_MASK_COLOR;
        delayMillis = Constants.DEFAULT_DELAY_MILLIS;
        fadeAnimationDuration = Constants.DEFAULT_FADE_DURATION;
        padding = Constants.DEFAULT_TARGET_PADDING;
        colorTextViewInfo = Constants.DEFAULT_COLOR_TEXTVIEW_INFO;
        isReady = false;
        isLayoutCompleted = false;
        isPerformClick = false;
        isIdempotent = false;
        isNavigationButton = true;


        eraser = new Paint();
        eraser.setColor(0xFFFFFFFF);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setFlags(Paint.ANTI_ALIAS_FLAG);


        /**
         * initialize objects
         */
        handler = new Handler();

        preferencesManager = new PreferencesManager(context);


        View layoutInfo = LayoutInflater.from(getContext()).inflate(R.layout.material_intro_card, null);

        infoView = layoutInfo.findViewById(R.id.info_layout);
        textViewInfo = (TextView) layoutInfo.findViewById(R.id.textview_info);
        leftImageView = (ImageView) layoutInfo.findViewById(R.id.leftImageView);
        rightImageView = (ImageView) layoutInfo.findViewById(R.id.rightImageView);
        AssetManager am = context.getApplicationContext().getAssets();
        Typeface custom_font = Typeface.createFromAsset(am, "font/david-font.ttf");
        textViewInfo.setTypeface(custom_font);
        textViewInfo.setTextColor(colorTextViewInfo);
        if (isNavigationButton) {
            bottomView = LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_view, null);
            indicatorView = (IndicatorView) bottomView.findViewById(R.id.circle_indicator_view);
            Button nextButton = (Button) bottomView.findViewById(R.id.nextButton);
            Button closeButton = (Button) bottomView.findViewById(R.id.closeButton);
            nextButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss(false);
                }
            });
            closeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss(true);
                }
            });
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                targetShape.reCalculateAll();
//                if (targetShape != null && targetShape.getPoint().y != 0 && !isLayoutCompleted) {
//                    setInfoLayout();
//                    removeOnGlobalLayoutListener(IntroView.this, this);
//                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < 16) {
            v.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            v.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady) return;

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            this.canvas = new Canvas(bitmap);
        }

        /**
         * Draw mask
         */
        this.canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        this.canvas.drawColor(maskColor);

        /**
         * Clear focus area
         */
        targetShape.draw(this.canvas, eraser, padding);

        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * Perform click operation when user
     * touches on target circle.
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (shouldShowIntro()) {

            Target targetView = getCurrentTarget();
            float xT = event.getX();
            float yT = event.getY();

            boolean isTouchOnFocus = targetShape.isTouchOnFocus(xT, yT);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (isTouchOnFocus && !isNavigationButton) {
                        targetView.getView().setPressed(true);
                        targetView.getView().invalidate();
                    }

                    return true;
                case MotionEvent.ACTION_UP:

                    if (isTouchOnFocus)
                        dismiss(false);

                    if (isTouchOnFocus && !isNavigationButton) {
                        targetView.getView().performClick();
                        targetView.getView().setPressed(true);
                        targetView.getView().invalidate();
                        targetView.getView().setPressed(false);
                        targetView.getView().invalidate();
                    }

                    return true;
                default:
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * Shows material view with fade in
     * animation
     */
    private void show() {

        if (shouldShowIntro()) {

            Target targetView = getCurrentTarget();
            Shape shape = new Rect(targetView, padding);
            setShape(shape);
            setTextViewInfo(targetView.getText());
            setInfoLayout();
            parentView.addView(this);
            setReady(true);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFirstTarget())
                        AnimationFactory.animateFadeIn(IntroView.this, fadeAnimationDuration,
                                new AnimationListener.OnAnimationStartListener() {
                                    @Override
                                    public void onAnimationStart() {
                                        setVisibility(VISIBLE);
                                    }
                                });
                    else
                        setVisibility(VISIBLE);
                }
            }, delayMillis);

        }
    }

    private boolean isFirstTarget() {
        return currentViewPos == 0;
    }

    private boolean shouldShowIntro() {
        return targetViewList != null && targetViewList.size() > currentViewPos;
    }

    private Target getCurrentTarget() {
        return targetViewList.get(currentViewPos);
    }

    /**
     * Dismiss Material Intro View
     */
    public void dismiss(boolean forceQuite) {
        if (forceQuite) {
            removeWithAnimation();
            return;
        }
        if (targetViewList.size() > currentViewPos + 1) {
            currentViewPos++;
            removeMaterialView();
            if (materialIntroListener != null)
                materialIntroListener.onUserClicked(introId);
            invalidate();
            show();
        } else {
            removeWithAnimation();
        }
    }

    private void removeWithAnimation() {
        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, new AnimationListener.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(GONE);
                removeMaterialView();
                if (materialIntroListener != null)
                    materialIntroListener.onUserClicked(introId);
            }
        });
    }

    private void removeMaterialView() {
        if (getParent() != null)
            ((ViewGroup) getParent()).removeView(this);
    }

    /**
     * locate info card view above/below the
     * circle. If circle's Y coordiante is bigger than
     * Y coordinate of root view, then locate cardview
     * above the circle. Otherwise locate below.
     */
    private void setInfoLayout() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                isLayoutCompleted = true;
                addInfoContentView();
                if (isNavigationButton) {
                    addButtonView();
                }
            }
        });
    }

    private void addInfoContentView() {
        RelativeLayout.LayoutParams infoDialogParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // set Max width
//                textViewInfo.setMaxWidth(width / 2);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // set margins
        int topMargin = 0;
        int bottomMargin = 0;
        if (targetShape.getPoint().y < height / 2) {
            topMargin = targetShape.getPoint().y + targetShape.getHeight() / 2;
            infoDialogParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.gravity = Gravity.TOP;
            leftImageView.setImageResource(R.drawable.left_top);
            rightImageView.setImageResource(R.drawable.right_top);
        } else {
            bottomMargin = height - (targetShape.getPoint().y
                    + targetShape.getHeight() / 2) + 2 * targetShape.getHeight() / 2;
            infoDialogParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.gravity = Gravity.BOTTOM;
            leftImageView.setImageResource(R.drawable.left_bottom);
            rightImageView.setImageResource(R.drawable.right_bottom);
        }
        leftImageView.setLayoutParams(params);
        rightImageView.setLayoutParams(params);

        int leftMargin = 0;
        int rightMargin = 0;
        int midPoint = (targetShape.getPoint().x);

        if (targetShape.getWidth() >= width || (targetShape.getWidth() + 2) >= width) {
            leftMargin = midPoint;
            infoDialogParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftImageView.setVisibility(VISIBLE);
            rightImageView.setVisibility(GONE);
        } else if (midPoint < width / 2) {
            leftMargin = midPoint;
            infoDialogParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            leftImageView.setVisibility(VISIBLE);
            rightImageView.setVisibility(GONE);
        } else {
            rightMargin = width - midPoint;
            infoDialogParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            leftImageView.setVisibility(GONE);
            rightImageView.setVisibility(VISIBLE);
        }
        infoDialogParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        infoView.setLayoutParams(infoDialogParams);
        infoView.postInvalidate();
        removeView(infoView);
        addView(infoView);

        infoView.setVisibility(VISIBLE);
    }

    private void addButtonView() {
        if (bottomView == null) {
            return;
        }
        if (bottomView.getParent() != null)
            ((ViewGroup) bottomView.getParent()).removeView(bottomView);
        Button nextButton = (Button) bottomView.findViewById(R.id.nextButton);
        Button closeButton = (Button) bottomView.findViewById(R.id.closeButton);
        int indicatorSize = targetViewList.size();
        if (indicatorSize <= 1) {
            nextButton.setVisibility(INVISIBLE);
            closeButton.setVisibility(INVISIBLE);
            indicatorView.setVisibility(INVISIBLE);
        } else {
            indicatorView.setVisibility(VISIBLE);
            indicatorView.setPageIndicators(indicatorSize);
            indicatorView.setCurrentPage(currentViewPos);
            nextButton.setVisibility(currentViewPos >= indicatorSize - 1 ? INVISIBLE : VISIBLE);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomView.setLayoutParams(params);
        bottomView.postInvalidate();

        addView(bottomView);
    }

    /**
     * SETTERS
     */

    private void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    private void setDelay(int delayMillis) {
        this.delayMillis = delayMillis;
    }


    private void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    private void addTarget(Target target) {
        targetViewList.add(target);
    }


    private void setShape(Shape shape) {
        this.targetShape = shape;
    }

    private void setPadding(int padding) {
        this.padding = padding;
    }

    private void setColorTextViewInfo(int colorTextViewInfo) {
        this.colorTextViewInfo = colorTextViewInfo;
        textViewInfo.setTextColor(this.colorTextViewInfo);
    }

    private void setTextViewInfo(String textViewInfo) {
        this.textViewInfo.setText(textViewInfo);
    }

    private void setTextViewInfoSize(int textViewInfoSize) {
        this.textViewInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, textViewInfoSize);
    }

    public void isNavigationButton(boolean isButton) {
        this.isNavigationButton = isButton;
    }


    private void setIdempotent(boolean idempotent) {
        this.isIdempotent = idempotent;
    }


    private void setListener(MaterialIntroListener materialIntroListener) {
        this.materialIntroListener = materialIntroListener;
    }

    private void setPerformClick(boolean isPerformClick) {
        this.isPerformClick = isPerformClick;
    }

    /**
     * Builder Class
     */
    public static class Builder {

        private IntroView materialIntroView;

        private Activity activity;


        public Builder(Activity activity) {
            this.activity = activity;
            materialIntroView = new IntroView(activity);
            materialIntroView.parentView = (ViewGroup) activity.getWindow().getDecorView();
        }

        public Builder setMaskColor(int maskColor) {
            materialIntroView.setMaskColor(maskColor);
            return this;
        }

        public Builder setDelayMillis(int delayMillis) {
            materialIntroView.setDelay(delayMillis);
            return this;
        }


        public Builder addTarget(View view, String text) {
            materialIntroView.addTarget(new ViewTarget(view, text));
            return this;
        }

        public Builder addTarget(ViewTarget viewTarget) {
            materialIntroView.addTarget(viewTarget);
            return this;
        }

        public Builder setTargetPadding(int padding) {
            materialIntroView.setPadding(padding);
            return this;
        }

        public Builder setTextColor(int textColor) {
            materialIntroView.setColorTextViewInfo(textColor);
            return this;
        }


        public Builder setInfoTextSize(int textSize) {
            materialIntroView.setTextViewInfoSize(textSize);
            return this;
        }

        public Builder setIsNavigationButton(boolean isNavigationButton) {
            materialIntroView.isNavigationButton(isNavigationButton);
            return this;
        }


        public Builder setIdempotent(boolean idempotent) {
            materialIntroView.setIdempotent(idempotent);
            return this;
        }


        public Builder setListene(MaterialIntroListener materialIntroListener) {
            materialIntroView.setListener(materialIntroListener);
            return this;
        }


        public Builder performClick(boolean isPerformClick) {
            materialIntroView.setPerformClick(isPerformClick);
            return this;
        }


        public IntroView show(String introId) {
            materialIntroView.introId = introId;
            if (!materialIntroView.preferencesManager.isDisplayed(introId)) {
                materialIntroView.preferencesManager.setDisplayed(introId);
                materialIntroView.show();
            }

            return materialIntroView;
        }

    }
}

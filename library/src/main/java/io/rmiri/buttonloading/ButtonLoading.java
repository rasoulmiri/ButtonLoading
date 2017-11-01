package io.rmiri.buttonloading;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

import io.rmiri.buttonloading.utils.DeviceScreenUtils;
import io.rmiri.buttonloading.utils.FontUtils;

import static io.rmiri.buttonloading.ButtonLoadingAttribute.STATE_ANIMATION_FINISH;
import static io.rmiri.buttonloading.ButtonLoadingAttribute.STATE_ANIMATION_START;
import static io.rmiri.buttonloading.ButtonLoadingAttribute.STATE_PROGRESS;


/**
 * Created by Rasoul Miri on 10/25/2017 .
 */

public class ButtonLoading extends View {

    private ButtonLoadingAttribute attribute = new ButtonLoadingAttribute();
    private OnButtonLoadingListener onButtonLoadingListener;

    private int height, width;

    private Paint paint;
    private RectF rect;
    Rect RectBoundCanvas;
    Point point;

    private ValueAnimator valueAnimatorCircleMain;
    private ValueAnimator valueAnimatorCircleSecond;

    private int valueAnimation1 = 0;
    private int valueAnimation2 = 0;
    private float fractionAnimation1 = 0.0f;
    private float fractionAnimation2 = 0.0f;
    private float fractionAnimation3 = 0.0f;

    private boolean isNeedFinishAnimation;
    private boolean isNeedAnimationBackground;


    ViewGroup parentView;
    ViewGroup rootView;
    ViewGroup.LayoutParams layoutParams;
    ImageButton buttonGetTouch;

    public ButtonLoading(Context context) {
        super(context);
        initView(context, null);
    }

    public ButtonLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ButtonLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    public void initView(Context context, AttributeSet attrs) {

        if (isInEditMode())
            return;

        //initial Attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.buttonLoading);
        attribute.setEnable(typedArray.getBoolean(R.styleable.buttonLoading_BL_enable, true));
        attribute.setIdParent(typedArray.getResourceId(R.styleable.buttonLoading_BL_idParent, 0));
        attribute.setFont(typedArray.getString(R.styleable.buttonLoading_BL_font));
        attribute.setText(typedArray.getString(R.styleable.buttonLoading_BL_text));
        attribute.setTextColor(typedArray.getColor(R.styleable.buttonLoading_BL_textColor, Color.WHITE));
        attribute.setBackgroundColor(typedArray.getColor(R.styleable.buttonLoading_BL_backgroundColor, Color.parseColor("#80ffffff")));
        attribute.setCircleColor(typedArray.getColor(R.styleable.buttonLoading_BL_circleColor, Color.parseColor("#00AFEF")));
        attribute.setCircleColorSecond(typedArray.getColor(R.styleable.buttonLoading_BL_circleColorSecond, Color.parseColor("#8000AFEF")));
        attribute.setTextSize(typedArray.getDimensionPixelSize(R.styleable.buttonLoading_BL_textSize, 14));
        attribute.setStateShow(typedArray.getInt(R.styleable.buttonLoading_BL_stateShow, ButtonLoadingAttribute.STATE_NORMAL));
        typedArray.recycle();

        //initial for canvas
        paint = new Paint();
        paint.setAntiAlias(true);
        rect = new RectF();
        point = new Point();
        RectBoundCanvas = new Rect();

        //view for get touch child in layout parent
        buttonGetTouch = new ImageButton(getContext());
        buttonGetTouch.setBackgroundColor(Color.TRANSPARENT);
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        buttonGetTouch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("setOnClickListener", "buttonGetTouch get touch");
            }
        });


        //onClickListener
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonLoadingListener != null
                        && attribute.isEnable()
                        && attribute.getStateShow() == ButtonLoadingAttribute.STATE_NORMAL) {

                    setProgress(true);
                    onButtonLoadingListener.onClick();
                }
            }
        });
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        if (width == 0) {
            width = xNew;
            height = yNew;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (parentView == null) {
            if (attribute.getIdParent() == 0) {
                parentView = (ViewGroup) getParent();
            } else {
                getRootView(this);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (attribute.getStateShow()) {

            case ButtonLoadingAttribute.STATE_NORMAL: {

                //circle main
                paint.setColor(attribute.getCircleColor());
                rect.set(0, 0, width, height);
                canvas.drawRoundRect(rect, height / 2, height / 2, paint);

                //text
                paintText();
                canvas.drawText(attribute.getText(), getXPositionByText(canvas), getYPositionByText(canvas), paint);

                break;
            }
            case STATE_ANIMATION_START: {

                //circle main
                paint.setColor(attribute.getCircleColor());
                point.set(canvas.getWidth() / 2, canvas.getHeight() / 2);
                int rectW = valueAnimation1;
                int rectH = height;
                int left = point.x - (rectW / 2);
                int top = point.y - (rectH / 2);
                int right = point.x + (rectW / 2);
                int bottom = point.y + (rectH / 2);
                rect.set(left, top, right, bottom);
                canvas.drawRoundRect(rect, height / 2, height / 2, paint);

                //text
                paintText();
                paint.setAlpha((int) ((1 - fractionAnimation1) * 255));
                canvas.drawText(attribute.getText(), getXPositionByText(canvas), getYPositionByText(canvas), paint);

                break;
            }
            case STATE_PROGRESS: {

                canvas.getClipBounds(RectBoundCanvas);
                RectBoundCanvas.inset(-DeviceScreenUtils.width(getContext()), -DeviceScreenUtils.height(getContext()));
                canvas.clipRect(RectBoundCanvas, Region.Op.REPLACE);

                //background
                paint.setColor(attribute.getBackgroundColor());
                point.set(canvas.getWidth() / 2, canvas.getHeight() / 2);

                if (!isNeedAnimationBackground) {
                    //draw rect
                    rect.set(-getX(), -getY(), DeviceScreenUtils.width(getContext()), DeviceScreenUtils.height(getContext()));
                    canvas.drawRoundRect(rect, 0, 0, paint);
                } else {
                    //draw oval
                    int rectWBackground = (int) (2 * DeviceScreenUtils.height(getContext()) * fractionAnimation3);
                    int rectHBackground = (int) (2 * DeviceScreenUtils.height(getContext()) * fractionAnimation3);
                    int leftBackground = point.x - (rectWBackground / 2);
                    int topBackground = point.y - (rectHBackground / 2);
                    int rightBackground = point.x + (rectWBackground / 2);
                    int bottomBackground = point.y + (rectHBackground / 2);
                    rect.set(leftBackground, topBackground, rightBackground, bottomBackground);
                    Path ovalPath = new Path();
                    ovalPath.addOval(rect, Path.Direction.CW);
                    canvas.drawPath(ovalPath, paint);
                }

                //circle second
                paint.setColor(attribute.getCircleColorSecond());
                int rectWAlpha = (int) (height * fractionAnimation2);
                int rectHAlpha = (int) (height * fractionAnimation2);
                int leftAlpha = point.x - (rectWAlpha / 2);
                int topAlpha = point.y - (rectHAlpha / 2);
                int rightAlpha = point.x + (rectWAlpha / 2);
                int bottomAlpha = point.y + (rectHAlpha / 2);
                rect.set(leftAlpha, topAlpha, rightAlpha, bottomAlpha);
                canvas.drawRoundRect(rect, 100, 100, paint);

                //circle main
                paint.setColor(attribute.getCircleColor());
                int rectW = valueAnimation1;
                int rectH = height;
                int left = point.x - (rectW / 2);
                int top = point.y - (rectH / 2);
                int right = point.x + (rectW / 2);
                int bottom = point.y + (rectH / 2);
                rect.set(left, top, right, bottom);
                canvas.scale(fractionAnimation1, fractionAnimation1, canvas.getWidth() / 2, canvas.getHeight() / 2);//scale canvas
                canvas.drawRoundRect(rect, height / 2, height / 2, paint);

                break;
            }
            case STATE_ANIMATION_FINISH: {

                //circle main
                paint.setColor(attribute.getCircleColor());
                point.set(canvas.getWidth() / 2, canvas.getHeight() / 2);
                int rectW = valueAnimation2;
                int rectH = height;
                int left = point.x - (rectW / 2);
                int top = point.y - (rectH / 2);
                int right = point.x + (rectW / 2);
                int bottom = point.y + (rectH / 2);
                rect.set(left, top, right, bottom);
                canvas.drawRoundRect(rect, height / 2, height / 2, paint);


                //text
                paintText();
                paint.setAlpha((int) ((fractionAnimation2) * 255));
                canvas.drawText(attribute.getText(), getXPositionByText(canvas), getYPositionByText(canvas), paint);

                canvas.scale(fractionAnimation1, fractionAnimation1, canvas.getWidth() / 2, canvas.getHeight() / 2);//scale canvas

                break;
            }
        }
    }

    private void paintText() {

        paint.setColor(attribute.getTextColor());
        paint.setTextSize(attribute.getTextSize());

        if (attribute.getFont() != null && !attribute.getFont().isEmpty()) {
            paint.setTypeface(FontUtils.getTypeface(getContext(), attribute.getFont()));
        }

    }

    int getXPositionByText(Canvas canvas) {
        return (canvas.getWidth() / 2 - ((int) paint.measureText(attribute.getText()) / 2));
    }

    int getYPositionByText(Canvas canvas) {
        return (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
    }

    public void setProgress(boolean isProgressing) {

        if (isProgressing) {
            if (attribute.getStateShow() != ButtonLoadingAttribute.STATE_NORMAL)
                return;
            animationStart();
            parentView.addView(buttonGetTouch, layoutParams);//add view for disable touch children
        } else {
            if (attribute.getStateShow() == ButtonLoadingAttribute.STATE_NORMAL)
                return;
            isNeedFinishAnimation = true;
        }

    }

    void animationStart() {

        attribute.setStateShow(STATE_ANIMATION_START);
        onButtonLoadingListener.onStart();

        ValueAnimator valueAnimatorLoading = ValueAnimator.ofInt(width, height);
        valueAnimatorLoading.setInterpolator(PathInterpolatorCompat.create(0.645f, 0.045f, 0.355f, 1f));
        valueAnimatorLoading.setDuration(225);
        valueAnimatorLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                valueAnimation1 = (int) (Integer) valueAnimator.getAnimatedValue();
                fractionAnimation1 = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        valueAnimatorLoading.start();

        valueAnimatorLoading.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    void animationProgress() {

        attribute.setStateShow(STATE_PROGRESS);

        fractionAnimation1 = 0.0f;
        fractionAnimation2 = 0.0f;
        fractionAnimation3 = 0.0f;

        isNeedAnimationBackground = true;

        bringToFront(); //push view to top all views

        final int[] countRepeat = {0};

        Interpolator pathInterpolatorCompat = PathInterpolatorCompat.create(0.455f, 0.030f, 0.515f, 0.955f);

        //circle Main
        valueAnimatorCircleMain = ValueAnimator.ofFloat(1.0f, 0.6f);
        valueAnimatorCircleMain.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorCircleMain.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimatorCircleMain.setInterpolator(pathInterpolatorCompat);
        valueAnimatorCircleMain.setDuration(525);
        valueAnimatorCircleMain.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fractionAnimation1 = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorCircleMain.start();

        valueAnimatorCircleMain.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                //don't allow animation hearth for Circle
                if (countRepeat[0] % 2 != 0 && isNeedAnimationBackground) {
                    isNeedAnimationBackground = false;
                }
            }
        });

        //circle second
        valueAnimatorCircleSecond = ValueAnimator.ofFloat(0.6f, 1.4f);
        valueAnimatorCircleSecond.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorCircleSecond.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimatorCircleSecond.setStartDelay(525);
        valueAnimatorCircleSecond.setInterpolator(pathInterpolatorCompat);
        valueAnimatorCircleSecond.setDuration(525);
        valueAnimatorCircleSecond.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fractionAnimation2 = (Float) animation.getAnimatedValue();
                fractionAnimation3 = animation.getAnimatedFraction();
            }
        });
        valueAnimatorCircleSecond.start();


        valueAnimatorCircleMain.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                countRepeat[0]++;
                if (countRepeat[0] % 2 != 0 & isNeedFinishAnimation) {
                    animationFinish();
                } else if (countRepeat[0] % 2 == 0 & isNeedFinishAnimation) {
                    isNeedAnimationBackground = true;
                }
            }
        });

    }

    void animationFinish() {

        //remove all animation for loading
        valueAnimatorCircleMain.removeAllListeners();
        valueAnimatorCircleMain.end();
        valueAnimatorCircleMain.cancel();
        valueAnimatorCircleSecond.removeAllListeners();
        valueAnimatorCircleSecond.end();
        valueAnimatorCircleSecond.cancel();

        attribute.setStateShow(STATE_ANIMATION_FINISH);

        fractionAnimation1 = 0.0f;

        ValueAnimator valueAnimatorFinish = ValueAnimator.ofInt(height, width);
        valueAnimatorFinish.setInterpolator(PathInterpolatorCompat.create(0.645f, 0.045f, 0.355f, 1f));
        valueAnimatorFinish.setDuration(225);
        valueAnimatorFinish.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                valueAnimation2 = (int) (Integer) valueAnimator.getAnimatedValue();
                fractionAnimation2 = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        valueAnimatorFinish.start();


        valueAnimatorFinish.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                attribute.setStateShow(ButtonLoadingAttribute.STATE_NORMAL);
                parentView.removeView(buttonGetTouch);
                isNeedFinishAnimation = false;
                onButtonLoadingListener.onFinish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public void getRootView(View v) {
        while (v.getParent() != null && v.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v.getParent();
            v = viewGroup;
            rootView = viewGroup;
        }
        setParentView(rootView);
    }

    private void setParentView(View v) {
        ViewGroup viewgroup = (ViewGroup) v;
        if (viewgroup.getId() == attribute.getIdParent()) {
            parentView = viewgroup;
        }
        for (int i = 0; i < viewgroup.getChildCount(); i++) {
            View v1 = viewgroup.getChildAt(i);
            if (v1 instanceof ViewGroup) setParentView(v1);
        }
    }

    //==============================================================================================
    //listener
    public void setOnButtonLoadingListener(OnButtonLoadingListener onButtonLoadingListener) {
        this.onButtonLoadingListener = onButtonLoadingListener;
    }

    public int getState() {
        return attribute.getStateShow();
    }

    public boolean isLoadingState() {
        return attribute.getStateShow() != ButtonLoadingAttribute.STATE_NORMAL;
    }

    public interface OnButtonLoadingListener {
        void onClick();

        void onStart();

        void onFinish();
    }

}

package com.app.checkmoney.CustomUi;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.app.checkmoney.Util.DevelopeLog;

/**
 * Created by Mhwan on 2016. 8. 25..
 */
public class ShowHideBehavior extends CoordinatorLayout.Behavior<View> {
    private int mDySinceDirectionChange;
    private boolean mIsShowing;
    private boolean mIsHiding;
    private static final Interpolator INTERPOLATOR = new FastOutLinearInInterpolator();
    private final int ANIMATION_TIME = 260;
    private final int distance = 4;

    public ShowHideBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        //세로방향의 스크롤일때만 true 반환
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mDySinceDirectionChange < 0 || dy < 0 && mDySinceDirectionChange > 0) {
            // 위 아래 스크롤 방향이 바뀔때는 동작을 취소하고 스크롤변화 값을 초기화 시킨다.
            child.animate().cancel();
            mDySinceDirectionChange = 0;
        }

        mDySinceDirectionChange += dy;

        DevelopeLog.d("dy변화값 : "+mDySinceDirectionChange);
        DevelopeLog.d("child height : "+child.getHeight());


        if (mDySinceDirectionChange > child.getHeight() + distance
                && child.getVisibility() == View.VISIBLE
                && !mIsHiding) {
            hideView(child);
        } else if (mDySinceDirectionChange < -distance
                && child.getVisibility() == View.GONE
                && !mIsShowing) {
            showView(child);
        }
    }

    /**
     * View를 아래로 내려가는 슬라이드 애니메이션을 보여준다.
     * 애니메이션이 종료되면 GONE으로 바꿔준다
     * @param view
     */
    private void hideView(final View view) {
        mIsHiding = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(view.getHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_TIME);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                // Prevent drawing the View after it is gone
                mIsHiding = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a hide should show the view
                mIsHiding = false;
                if (!mIsShowing) {
                    showView(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        animator.start();
    }

    /**
     * View를 아래로부터 슬라이드업 애니메이션을 보여준다.
     * 애니메이션이 시작할때 VISIBLE 상태로해서 슬라이드업
     *
     * @param view The quick return view
     */
    private void showView(final View view) {
        mIsShowing = true;
        ViewPropertyAnimator animator = view.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(ANIMATION_TIME);

        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mIsShowing = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Canceling a show should hide the view
                mIsShowing = false;
                if (!mIsHiding) {
                    hideView(view);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        animator.start();
    }
}

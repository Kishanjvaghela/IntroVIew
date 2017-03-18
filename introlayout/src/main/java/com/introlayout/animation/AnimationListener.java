package com.introlayout.animation;

/**
 * Created by kisha_000 on 3/1/2017.
 */

public interface AnimationListener {
    /**
     * We need to make MaterialIntroView visible
     * before fade in animation starts
     */
    interface OnAnimationStartListener {
        void onAnimationStart();
    }

    /**
     * We need to make MaterialIntroView invisible
     * after fade out animation ends.
     */
    interface OnAnimationEndListener {
        void onAnimationEnd();
    }

}

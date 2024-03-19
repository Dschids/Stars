package com.example.stars

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotator()
        }

        translateButton.setOnClickListener {
            translator()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun rotator() {
        /* creates an animation of the star rotating 360, star is what is being animated,
        View.ROTATION tells what the animation is, -360f is the start value, 0f is the end
         */
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
        // how long the animation will take in milliseconds
        animator.duration = 1000
        animator.disableDuringAnimation(rotateButton)
        animator.start()
    }

    private fun translator() {
        /* creates an animation of the star moving right 200f than back to its original position
        using a repeat
        */
        val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f)
        // the animation is going to repeat 1 time, essentially just run once
        animator.repeatCount = 1
        // tells the animation to essentially run in reverse once it ends
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableDuringAnimation(translateButton)
        animator.start()
    }

    private fun scaler() {
        // PropertyValuesHolder used to change multiple things during a single animation
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star, scaleX,scaleY)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableDuringAnimation(scaleButton)
        animator.start()

    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        var animator = ObjectAnimator.ofArgb(star.parent, "backgroundColor",
            Color.BLACK, Color.RED)
        animator.setDuration(500)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        val container = star.parent as ViewGroup

        // get the width and height of the frame layout that holds the star
        val containerW = container.width
        val containerH = container.height

        // get the width and height of the star image
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        // create a new View to hold the star graphic
        val newStar = AppCompatImageView(this)
        // set the image contained in newStar
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT)
        container.addView(newStar)

        // set the starting scale of newStar, scaleX is part of the View library
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW = newStar.scaleX
        starH = newStar.scaleY

        // set the starting location
        newStar.translationX = Math.random().toFloat() * containerW - starW/2

        // animators to move stars
        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH - starH)
        // accelerates the star from top to bottom
        mover.interpolator = AccelerateInterpolator(1f)

        // animator to rotate the star a random amount
        val rotator = ObjectAnimator.ofFloat(newStar, View.ROTATION,
            (Math.random() * 1080).toFloat())
        // rotate the stars
        rotator.interpolator = LinearInterpolator()
        // AnimatorSet lets you play multiple animations together
        val set = AnimatorSet()
        // what animations to play
        set.playTogether(mover, rotator)
        // how long to play them
        set.duration = (Math.random() * 1500 + 500).toLong()

        // add a listener to detect the end of the animation then delete the created newStar view
        set.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(newStar)
            }
        })
        set.start()

    }

    /* using ObjectAnimator.disableDuringAnimation, means that this function is an extension of
    ObjectAnimator meaning after we define our animator variable we can use
    animator.disableDuringAnimation to call the function and only have to pass the view(button)
    that will be disabled
     */
    private fun ObjectAnimator.disableDuringAnimation(view: View){
        addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator) {
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator) {
                view.isEnabled = true
            }
        })
    }
}

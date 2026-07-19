package com.example.portingkotlin

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.`object`.conversation.BodyLanguageOption
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlin.concurrent.thread


class SentenceReplier {


    companion object {
        val sleep = 4000.toLong()
        val default2 = 500
        val default4 = 283
        val fadeDuration = 500 // 500 millisecondi (mezzo secondo)

        val language = "it"
        val emulator = true
        val pause = 750L

        val shape = GradientDrawable()
        lateinit var mainAct: MainActivity

        private lateinit var label: TextView

        lateinit var myButton: Button
        var pressed = false
        var sentenceTxt = ""

        fun replyTo(
            qiContext: QiContext,
            sentence: Int,
            voice_image_mode: Int,
            pronunciation_mode: Int,
            image_amount_mode: Int,
            image_modifier_mode: Int,
            layout: LinearLayout,
            mainActivity: MainActivity
        ) {
            val textView = AppCompatTextView(mainActivity)
            //textView.text = ""
            mainAct = mainActivity
            shape.cornerRadius = 20f // Imposta la curvatura degli angoli
            shape.setColor(Color.parseColor("#ADD8E6"))

            val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 92)
            textView.gravity = Gravity.CENTER
            textView.textSize = 24f // Imposta la dimensione del testo
            textView.setTypeface(null, Typeface.BOLD) // Imposta il testo in grassetto
            textView.setTextColor(ContextCompat.getColor(mainActivity, android.R.color.white))
            textView.layoutParams = textParams
            textView.setBackgroundColor(
                ContextCompat.getColor(
                    mainActivity,
                    android.R.color.holo_blue_light
                )
            )
            label = textView
            label.typeface = ResourcesCompat.getFont(mainActivity, R.font.gotham_book)!!

            Handler(Looper.getMainLooper()).post {
                clearPage(layout)
            }

            myButton = Button(mainActivity)
            myButton.text = "Avanti"
            val gothamFontBook = ResourcesCompat.getFont(mainActivity, R.font.gotham_book)!!
            myButton.typeface = gothamFontBook
            myButton.background = shape

            myButton.setOnClickListener {
                pressed = true
                val x = myButton.parent as ViewGroup
                x.removeView(myButton)
            }

            sentenceTxt = ""
            if (sentence == 1) {
                Handler(Looper.getMainLooper()).post {
                    textView.text = "Comunicazione multi-modale"
                }
                replayBrothers(qiContext, layout, mainActivity)
            } else if (sentence == 2) {
                if (image_amount_mode == 2) {
                    replayHamburger(qiContext, layout, mainActivity, image_modifier_mode)
                } else {
                    replaySchool(qiContext, layout, mainActivity, image_modifier_mode)
                }
            } else if (sentence == 3) {
                replayAsteroid(qiContext, layout, mainActivity, voice_image_mode)
            } else if (sentence == 4) {
                replayLove(qiContext, layout, mainActivity, pronunciation_mode)
            } else if (sentence == 5) {
                replayBath(
                    qiContext,
                    layout,
                    mainActivity,
                    pronunciation_mode + voice_image_mode + image_modifier_mode + image_amount_mode
                )
            }

            while (!pressed) {
                Thread.sleep(100)
            }
            pressed = false
        }

        private fun replayBath(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity,
            complexity: Int
        ) {
            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topRow.layoutParams = topRowParams

            val col = LinearLayout(mainActivity)
            col.orientation = LinearLayout.VERTICAL
            val colP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            col.layoutParams = colP

            val botRow = LinearLayout(mainActivity)
            botRow.orientation = LinearLayout.HORIZONTAL
            val botRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            botRow.layoutParams = botRowParams

            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)
            val separeo = ImageView(mainActivity)
            val image3 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            image1.setImageResource(R.drawable.bagn)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params1_1.setMargins(178, 35, 178, 0) // Imposta la posizione
            val params1_2 = LinearLayout.LayoutParams(default2, default2) // Imposta le dimensioni
            params1_2.setMargins(70, 103, 70, 0) // Imposta la posizione

            if (complexity == 4) {
                image1.layoutParams = params1_1
            } else {
                image1.layoutParams = params1_2
            }

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.oli)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params2.setMargins(178, 35, 0, 0) // Imposta la posizione
            image2.layoutParams = params2

            // Imposta l'immagine separeo
            separeo.setImageResource(R.drawable.giardin)
            val params_separeo = LinearLayout.LayoutParams(0, 353) // Imposta le dimensioni
            params_separeo.setMargins(0, 0, 0, 0) // Imposta la posizione
            separeo.layoutParams = params_separeo


            // Imposta l'immagine 3
            image3.setImageResource(R.drawable.polver)
            image3.scaleType = ImageView.ScaleType.FIT_XY
            val params3_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params3_1.setMargins(499, 35, 324, 0) // Imposta la posizione
            val params3_2 = LinearLayout.LayoutParams(default2, default2) // Imposta le dimensioni
            params3_2.setMargins(70, 103, 70, 0) // Imposta la posizione

            if (complexity == 4) {
                image3.layoutParams = params3_1
            } else {
                image3.layoutParams = params3_2
            }

            if (complexity == 4) {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Tre immagini - Senza pausa"
                    }
                }
                thread {
                    Thread.sleep(800)

                    Handler(Looper.getMainLooper()).post {

                        // Aggiungi le immagini alla prima riga
                        topRow.addView(separeo)
                        topRow.addView(image1)
                        addFadeInAnimation(image1, fadeDuration)

                        // Aggiungi le righe
                        layout.addView(topRow)
                        layout.addView(botRow)
                    }

                    Thread.sleep(550)

                    Handler(Looper.getMainLooper()).post {
                        topRow.addView(image2)
                        addFadeInAnimation(image2, fadeDuration)
                    }

                    Thread.sleep(3650)

                    Handler(Looper.getMainLooper()).post {
                        botRow.addView(image3)
                        addFadeInAnimation(image3, fadeDuration)
                    }

                    Thread.sleep(sleep)

                    Handler(Looper.getMainLooper()).post {
                        val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                        paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                        myButton.layoutParams = paramB
                        botRow.addView(myButton)
                    }
                }

                saySentence(
                    "Un grande bagno di olio mi farà proprio bene. Ho una forma così grave di contaminazione da polvere che stento a muovermi",
                    qiContext
                )
                saySentenceEN(
                    "A big bath in oil will really do me good. I have such a severe form of dust contamination that I can hardly move",
                    qiContext
                )
            } else {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Due immagini - Con pausa"
                    }
                }
                saySentence("Un grande bagno di olio", qiContext)

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(separeo)
                    topRow.addView(image1)
                    addFadeInAnimation(image1, fadeDuration)

                    // Aggiungi le righe
                    layout.addView(topRow)
                }

                saySentence(
                    "mi farà proprio bene. Ho una forma così grave di contaminazione da polvere",
                    qiContext
                )
                saySentenceEN(
                    "will really do me good. I have such a severe form of dust contamination",
                    qiContext
                )

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    col.addView(image3)
                    topRow.addView(col)
                    addFadeInAnimation(image3, fadeDuration)

                }

                saySentence("che stento a muovermi", qiContext)
                saySentenceEN("that I can hardly move", qiContext)

                Thread.sleep(sleep)

                Handler(Looper.getMainLooper()).post {
                    val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                    paramB.setMargins(490, 25, 0, 0) // Imposta la posizione
                    myButton.layoutParams = paramB
                    col.addView(myButton)
                }

            }
        }


        private fun replayLove(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity,
            pronunciationMode: Int
        ) {
            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topRow.layoutParams = topRowParams

            val botRow = LinearLayout(mainActivity)
            botRow.orientation = LinearLayout.HORIZONTAL
            val botRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            botRow.layoutParams = botRowParams

            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)
            val separeo = ImageView(mainActivity)
            val image3 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            loadGifIntoImageView(image1, R.drawable.innamor3, mainActivity)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params1_1.setMargins(178, 35, 178, 0) // Imposta la posizione
            image1.layoutParams = params1_1

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.voc)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params2.setMargins(178, 35, 0, 0) // Imposta la posizione
            image2.layoutParams = params2

            // Imposta l'immagine separeo
            separeo.setImageResource(R.drawable.giardin)
            val params_separeo = LinearLayout.LayoutParams(0, 353) // Imposta le dimensioni
            params_separeo.setMargins(0, 0, 0, 0) // Imposta la posizione
            separeo.layoutParams = params_separeo


            // Imposta l'immagine 3
            image3.setImageResource(R.drawable.amor)
            image3.scaleType = ImageView.ScaleType.FIT_XY
            val params3_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params3_1.setMargins(499, 35, 324, 0) // Imposta la posizione
            image3.layoutParams = params3_1

            if (pronunciationMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Pronuncia con pause"
                    }
                }
                saySentence("Sono innamorato", qiContext)
                saySentenceEN("I am in love", qiContext)

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(separeo)
                    topRow.addView(image1)
                    addFadeInAnimation(image1, fadeDuration)

                    // Aggiungi le righe
                    layout.addView(topRow)
                    layout.addView(botRow)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("di altre seicento quarantuno voci", qiContext)
                saySentenceEN("with six hundred and forty-one other voices", qiContext)

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(image2)
                    addFadeInAnimation(image2, fadeDuration)

                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("ma questo non incide sul mio amore", qiContext)
                saySentenceEN("but this does not affect my love for you", qiContext)

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    botRow.addView(image3)
                    addFadeInAnimation(image3, fadeDuration)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("per te che è unico", qiContext)
                saySentenceEN("which is unique", qiContext)

                Thread.sleep(sleep)

                Handler(Looper.getMainLooper()).post {
                    val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                    paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                    myButton.layoutParams = paramB
                    botRow.addView(myButton)
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Pronuncia senza pausa"
                    }
                }
                thread {
                    Thread.sleep(1000)

                    Handler(Looper.getMainLooper()).post {

                        // Aggiungi le immagini alla prima riga
                        topRow.addView(separeo)
                        topRow.addView(image1)
                        addFadeInAnimation(image1, fadeDuration)

                        // Aggiungi le righe
                        layout.addView(topRow)
                        layout.addView(botRow)
                    }

                    Thread.sleep(1700)

                    Handler(Looper.getMainLooper()).post {
                        topRow.addView(image2)
                        addFadeInAnimation(image2, fadeDuration)
                    }

                    Thread.sleep(1800)

                    Handler(Looper.getMainLooper()).post {
                        botRow.addView(image3)
                        addFadeInAnimation(image3, fadeDuration)
                    }

                    Thread.sleep(sleep)

                    Handler(Looper.getMainLooper()).post {
                        val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                        paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                        myButton.layoutParams = paramB
                        botRow.addView(myButton)
                    }
                }

                saySentence(
                    "Sono innamorato di altre seicentoquarantuno voci ma questo non incide sul mio amore per te che è unico",
                    qiContext
                )
                saySentenceEN(
                    "I am in love with six hundred and forty-one other voices but this does not affect my love for you which is unique",
                    qiContext
                )

            }
        }

        private fun replayAsteroid(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity,
            voiceImageMode: Int
        ) {

            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topRow.layoutParams = topRowParams

            val botRow = LinearLayout(mainActivity)
            botRow.orientation = LinearLayout.HORIZONTAL
            val botRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            botRow.layoutParams = botRowParams

            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)
            val separeo = ImageView(mainActivity)
            val image3 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            loadGifIntoImageView(image1, R.drawable.navig, mainActivity)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params1_1.setMargins(178, 35, 178, 0) // Imposta la posizione
            image1.layoutParams = params1_1

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.camp)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params2.setMargins(178, 35, 0, 0) // Imposta la posizione
            image2.layoutParams = params2

            // Imposta l'immagine separeo
            separeo.setImageResource(R.drawable.giardin)
            val params_separeo = LinearLayout.LayoutParams(0, 353) // Imposta le dimensioni
            params_separeo.setMargins(0, 0, 0, 0) // Imposta la posizione
            separeo.layoutParams = params_separeo


            // Imposta l'immagine 3
            image3.setImageResource(R.drawable.asteroid)
            image3.scaleType = ImageView.ScaleType.FIT_XY
            val params3_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params3_1.setMargins(499, 35, 324, 0) // Imposta la posizione
            image3.layoutParams = params3_1

            if (voiceImageMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Immagini a fine frase"
                    }
                }
                saySentence(
                    "La possibilità di navigare con successo in un campo di asteroidi è circa una su tremilasettecentoventi",
                    qiContext
                )
                saySentenceEN(
                    "The chance of navigating successfully a field of asteroids is about one in three thousand seven hundred and twenty",
                    qiContext
                )
                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(separeo)
                    topRow.addView(image1)
                    addFadeInAnimation(image1, fadeDuration)
                    topRow.addView(image2)
                    addFadeInAnimation(image2, fadeDuration)
                    botRow.addView(image3)
                    addFadeInAnimation(image3, fadeDuration)

                    // Aggiungi le righe
                    layout.addView(topRow)
                    layout.addView(botRow)
                }
            } else if (voiceImageMode == 2) {
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Immagini simultanee alle parole"
                    }
                }
                saySentence("La possibilità di navigare", qiContext)
                saySentenceEN("The chance of navigating", qiContext)

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(separeo)
                    topRow.addView(image1)
                    addFadeInAnimation(image1, fadeDuration)

                    // Aggiungi le righe
                    layout.addView(topRow)
                    layout.addView(botRow)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("con successo in un campo", qiContext)
                saySentenceEN("successfully a field", qiContext)

                Handler(Looper.getMainLooper()).post {
                    topRow.addView(image2)
                    addFadeInAnimation(image2, fadeDuration)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("di asteroidi", qiContext)
                saySentenceEN("of asteroids", qiContext)

                Handler(Looper.getMainLooper()).post {
                    botRow.addView(image3)
                    addFadeInAnimation(image3, fadeDuration)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("è circa una su tremilasettecentoventi", qiContext)
                saySentenceEN("is about one in three thousand seven hundred and twenty", qiContext)
            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Immagini a blocchi di dipendenze sintattiche"
                }
                saySentence(
                    "La possibilità di navigare con successo in un campo di asteroidi",
                    qiContext
                )
                saySentenceEN(
                    "The chance of navigating successfully a field of asteroids",
                    qiContext
                )

                Handler(Looper.getMainLooper()).post {

                    // Aggiungi le immagini alla prima riga
                    topRow.addView(separeo)
                    topRow.addView(image1)
                    addFadeInAnimation(image1, fadeDuration)
                    topRow.addView(image2)
                    addFadeInAnimation(image2, fadeDuration)
                    botRow.addView(image3)
                    addFadeInAnimation(image3, fadeDuration)

                    // Aggiungi le righe
                    layout.addView(topRow)
                    layout.addView(botRow)
                }
                if (emulator) {
                    Thread.sleep(pause)
                }

                saySentence("è circa una su tremilasettecentoventi", qiContext)
                saySentenceEN("is about one in three thousand seven hundred and twenty", qiContext)
            }


            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                myButton.layoutParams = paramB
                botRow.addView(myButton)
            }
        }

        private fun replaySchool(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity,
            imageModifierMode: Int
        ) {
            saySentence("I bambini giocano con una piccola palla da calcio", qiContext)
            saySentenceEN("Children play with a small ball of football", qiContext)

            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topRow.layoutParams = topRowParams

            val botRow = LinearLayout(mainActivity)
            botRow.orientation = LinearLayout.HORIZONTAL
            val botRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            botRow.layoutParams = botRowParams

            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)
            val separeo = ImageView(mainActivity)
            val image3 = ImageView(mainActivity)
            val image4 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            image1.setImageResource(R.drawable.ball)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params1_1.setMargins(178, 35, 178, 0) // Imposta la posizione
            val params1_2 = LinearLayout.LayoutParams(213, 213) // Imposta le dimensioni
            params1_2.setMargins(213, 70, 213, 0) // Imposta la posizione

            if (imageModifierMode == 1) {
                image1.layoutParams = params1_2
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Con modificatori - 4 immagini"
                    }
                }
            } else {
                image1.layoutParams = params1_1
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Senza modificatori - 4 immagini"
                    }
                }
            }

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.calci)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params2.setMargins(178, 35, 0, 0) // Imposta la posizione
            image2.layoutParams = params2

            // Imposta l'immagine 2
            separeo.setImageResource(R.drawable.giardin)
            val params_separeo = LinearLayout.LayoutParams(0, 353) // Imposta le dimensioni
            params_separeo.setMargins(0, 0, 0, 0) // Imposta la posizione
            separeo.layoutParams = params_separeo


            // Imposta l'immagine 3
            image3.setImageResource(R.drawable.giardin)
            image3.scaleType = ImageView.ScaleType.FIT_XY
            val params3_1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params3_1.setMargins(178, 35, 178, 0) // Imposta la posizione
            val params3_2 = LinearLayout.LayoutParams(353, 353) // Imposta le dimensioni
            params3_2.setMargins(143, 0, 143, 0) // Imposta la posizione

            if (imageModifierMode == 1) {
                image3.layoutParams = params3_2
            } else {
                image3.layoutParams = params3_1
            }

            // Imposta l'immagine 4
            image4.setImageResource(R.drawable.scuol)
            image4.scaleType = ImageView.ScaleType.FIT_XY
            val params4 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params4.setMargins(178, 35, 0, 0) // Imposta la posizione
            image4.layoutParams = params4

            Handler(Looper.getMainLooper()).post {

                // Aggiungi le immagini alla prima riga
                topRow.addView(separeo)
                topRow.addView(image1)
                addFadeInAnimation(image1, fadeDuration)
                topRow.addView(image2)
                addFadeInAnimation(image2, fadeDuration)

                // Aggiungi le righe
                layout.addView(topRow)
                layout.addView(botRow)
            }
            if (emulator) {
                Thread.sleep(pause)
            }

            saySentence("nel giardino grande della scuola", qiContext)
            saySentenceEN("in the large garden of the school", qiContext)

            Handler(Looper.getMainLooper()).post {
                botRow.addView(image3)
                addFadeInAnimation(image3, fadeDuration)
                botRow.addView(image4)
                addFadeInAnimation(image4, fadeDuration)
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                myButton.layoutParams = paramB
                botRow.addView(myButton)
            }
        }

        private fun replayHamburger(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity,
            imageModifierMode: Int
        ) {
            saySentence("Io mangio hamburger grandi di cibernetica", qiContext)
            saySentenceEN("I eat big cyber burgers", qiContext)

            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            //topRow.layoutParams = topRowParams

            val col = LinearLayout(mainActivity)
            col.orientation = LinearLayout.VERTICAL
            val colP = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            col.layoutParams = colP


            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            image1.setImageResource(R.drawable.hamburger)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1_1 = LinearLayout.LayoutParams(default2, default2) // Imposta le dimensioni
            params1_1.setMargins(70, 103, 70, 0) // Imposta la posizione
            val params1_2 = LinearLayout.LayoutParams(640, 640) // Imposta le dimensioni
            params1_2.setMargins(0, 33, 0, 0) // Imposta la posizione

            if (imageModifierMode == 1) {
                image1.layoutParams = params1_2
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Con modificatori - 2 immagini"
                    }
                }
            } else {
                image1.layoutParams = params1_1
                Handler(Looper.getMainLooper()).post {
                    if (language == "it") {
                        label.text = "Senza modificatori - 2 immagini"
                    }
                }
            }

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.pizz)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2_1 = LinearLayout.LayoutParams(default2, default2) // Imposta le dimensioni
            params2_1.setMargins(70, 103, 0, 0) // Imposta la posizione
            val params2_2 = LinearLayout.LayoutParams(380, 380) // Imposta le dimensioni
            params2_2.setMargins(165, 163, 95, 60) // Imposta la posizione

            if (imageModifierMode == 1) {
                image2.layoutParams = params2_2
            } else {
                image2.layoutParams = params2_1
            }


            Handler(Looper.getMainLooper()).post {

                topRow.addView(image1)
                addFadeInAnimation(image1, fadeDuration)

                // Aggiungi le righe
                layout.addView(topRow)
            }
            if (emulator) {
                Thread.sleep(pause)
            }

            saySentence("e pizze piccole di matematica", qiContext)
            saySentenceEN("and very small maths pizzas", qiContext)

            Handler(Looper.getMainLooper()).post {
                col.addView(image2)
                topRow.addView(col)
                addFadeInAnimation(image2, fadeDuration)
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                paramB.setMargins(490, 25, 0, 0) // Imposta la posizione
                myButton.layoutParams = paramB
                col.addView(myButton)
            }
        }

        private fun replayBrothers(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity
        ) {
            saySentence("Io ho quattro fratelli. Loro si chiamano Nao", qiContext)
            saySentenceEN("I have four brothers. Their names are Nao", qiContext)

            val topRow = LinearLayout(mainActivity)
            topRow.orientation = LinearLayout.HORIZONTAL
            val topRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            topRow.layoutParams = topRowParams

            val botRow = LinearLayout(mainActivity)
            botRow.orientation = LinearLayout.HORIZONTAL
            val botRowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            botRow.layoutParams = botRowParams

            val image1 = ImageView(mainActivity)
            val image2 = ImageView(mainActivity)
            val separeo = ImageView(mainActivity)
            val image3 = ImageView(mainActivity)
            val image4 = ImageView(mainActivity)

            // Imposta le immagini
            // Imposta l'immagine 1
            image1.setImageResource(R.drawable.nao)
            image1.scaleType = ImageView.ScaleType.FIT_XY
            val params1 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params1.setMargins(178, 35, 0, 0) // Imposta la posizione

            image1.layoutParams = params1

            // Imposta l'immagine 2
            image2.setImageResource(R.drawable.icub)
            image2.scaleType = ImageView.ScaleType.FIT_XY
            val params2 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params2.setMargins(357, 35, 0, 0) // Imposta la posizione
            image2.layoutParams = params2

            // Imposta l'immagine 2
            separeo.setImageResource(R.drawable.icub)
            val params_separeo = LinearLayout.LayoutParams(0, 353) // Imposta le dimensioni
            params_separeo.setMargins(0, 0, 0, 0) // Imposta la posizione
            separeo.layoutParams = params_separeo


            // Imposta l'immagine 3
            image3.setImageResource(R.drawable.sanbot)
            image3.scaleType = ImageView.ScaleType.FIT_XY
            val params3 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params3.setMargins(178, 35, 0, 0) // Imposta la posizione
            image3.layoutParams = params3

            // Imposta l'immagine 4
            image4.setImageResource(R.drawable.r1)
            image4.scaleType = ImageView.ScaleType.FIT_XY
            val params4 = LinearLayout.LayoutParams(default4, default4) // Imposta le dimensioni
            params4.setMargins(357, 35, 0, 0) // Imposta la posizione
            image4.layoutParams = params4

            Handler(Looper.getMainLooper()).post {

                // Aggiungi le immagini alla prima riga
                topRow.addView(separeo)
                topRow.addView(image1)
                addFadeInAnimation(image1, fadeDuration)

                // Aggiungi le righe
                layout.addView(topRow)
                layout.addView(botRow)
            }
            if (emulator) {
                Thread.sleep(pause)
            }

            saySentence("Icab", qiContext)
            saySentenceEN("Icub", qiContext)

            Handler(Looper.getMainLooper()).post {
                topRow.addView(image2)
                addFadeInAnimation(image2, fadeDuration)
            }
            if (emulator) {
                Thread.sleep(pause)
            }

            saySentence("Sanbot", qiContext)
            saySentenceEN("Sandbot", qiContext)

            Handler(Looper.getMainLooper()).post {
                botRow.addView(image3)
                addFadeInAnimation(image3, fadeDuration)
            }
            if (emulator) {
                Thread.sleep(pause)
            }

            saySentence("e R1", qiContext)
            saySentenceEN("and R1", qiContext)

            Handler(Looper.getMainLooper()).post {
                botRow.addView(image4)
                addFadeInAnimation(image4, fadeDuration)
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75) // Imposta le dimensioni
                paramB.setMargins(25, 278, 0, 0) // Imposta la posizione
                myButton.layoutParams = paramB
                botRow.addView(myButton)
            }

        }

        private fun addFadeInAnimation(view: ImageView, duration: Int) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = duration.toLong()
            view.startAnimation(fadeIn)
        }

        private fun loadGifIntoImageView(
            imageView: ImageView,
            resourceId: Int,
            mainActivity: MainActivity
        ) {
            Glide.with(mainActivity)
                .asGif()
                .load(resourceId)
                .into(object : CustomTarget<GifDrawable>() {

                    override fun onResourceReady(
                        resource: GifDrawable,
                        transition: Transition<in GifDrawable>?
                    ) {
                        imageView.setImageDrawable(resource)
                        resource.start()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }

        private fun clearPage(layout: LinearLayout) {
            layout.removeAllViews()
            label.text = ""
            layout.addView(label)
        }

        fun saySentence(s: String, qiContext: QiContext) {
            if (language == "it") {
                val locale = Locale(Language.ITALIAN, Region.ITALY)
                SayBuilder.with(qiContext).withText(s).withLocale(locale)
                    .withBodyLanguageOption(BodyLanguageOption.DISABLED).build().run()
            }
        }

        private fun saySentenceEN(s: String, qiContext: QiContext) {
            if (language == "en") {
                updateLabel(s)
                //val locale = Locale(Language.ENGLISH, Region.UNITED_KINGDOM)
                //SayBuilder.with(qiContext).withText(s).withLocale(locale).withBodyLanguageOption(BodyLanguageOption.DISABLED).build().run()
            }
        }

        private fun updateLabel(s: String) {
            //thread {
                //Handler(Looper.getMainLooper()).post {

                val words: List<String> = s.split(" ") // splits by whitespace

                for (word in words) {
                    sentenceTxt = "$sentenceTxt $word"
                    mainAct.runOnUiThread {
                        label.text = sentenceTxt
                    }
                    estimateWait(word)
                }

        }

        private fun estimateWait(word: String) {
            val wait = word.length * 40 + 20
            Thread.sleep(wait.toLong())
        }
    }

}
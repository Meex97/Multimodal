package com.example.svgporting

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
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
import java.io.ByteArrayInputStream
import java.io.InputStream
import com.caverock.androidsvg.SVG
import kotlin.concurrent.thread


class SentenceReplier {


    companion object {
        val sleep = 3000.toLong()
        val default2 = 500
        val default4 = 283
        val fadeDuration = 500  // mills

        val language = "it"
        val emulator = false
        val pause = 750L

        val shape = GradientDrawable()
        lateinit var mainAct: MainActivity

        private lateinit var label: TextView
        lateinit var imageView: ImageView
        lateinit var btnNext: Button
        lateinit var btnRepeat: Button
        var pressed = false
        var pressedRepeat = false
        var sentenceTxt = ""

        fun replyTo(
            qiContext: QiContext,
            sentence: Int,
            voice_image_mode: Int,
            pronunciation_mode: Int,
            changes_amount_mode: Int,
            image_modifier_mode: Int,
            color_mode: Int,
            layout: LinearLayout,
            mainActivity: MainActivity
        ) {
            val gothamFontBook = ResourcesCompat.getFont(mainActivity, R.font.gotham_book)!!
            val textView = AppCompatTextView(mainActivity)
            btnNext = Button(mainActivity)
            btnRepeat = Button(mainActivity)

            mainActivity.runOnUiThread {
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

                clearPage(layout)

                btnNext.text = "Avanti"
                btnNext.typeface = gothamFontBook
                btnNext.background = shape

                btnRepeat.text = "Ripeti"
                btnRepeat.typeface = gothamFontBook
                btnRepeat.background = shape
            }


            btnNext.setOnClickListener {
                pressed = true
                val x = btnNext.parent as ViewGroup
                x.removeView(btnNext)
            }

            btnRepeat.setOnClickListener {
                pressedRepeat = true
                val x = btnRepeat.parent as ViewGroup
                x.removeView(btnRepeat)
            }

            sentenceTxt = ""
            if (sentence == 1) {
                Handler(Looper.getMainLooper()).post {
                    textView.text = "Comunicazione multimodale"
                }

                replyIntro(qiContext, layout, mainActivity)
            } else if (sentence == 2) {
                if (changes_amount_mode == 2) {
                    replyFewChanges(qiContext, layout, mainActivity, image_modifier_mode)
                } else {
                    replyManyChanges(qiContext, layout, mainActivity, image_modifier_mode)
                }
            } else if (sentence == 3) {
                replyVoiceImageCoordination(qiContext, layout, mainActivity, voice_image_mode)
            } else if (sentence == 4) {
                replyPronounce(qiContext, layout, mainActivity, pronunciation_mode)
            } else if (sentence == 5) {
                replyColors(qiContext, layout, mainActivity, color_mode)
            } else if (sentence == 6) {
                replyComplexity(qiContext, layout, mainActivity,pronunciation_mode + voice_image_mode + image_modifier_mode + changes_amount_mode)
            }

            while (!pressed && !pressedRepeat) {
                Thread.sleep(100)
            }
            pressed = false
            if (pressedRepeat) {
                pressedRepeat = false
                replyTo(qiContext, sentence, voice_image_mode, pronunciation_mode, changes_amount_mode, image_modifier_mode, color_mode, layout, mainActivity)
            }
        }

        private fun replyColors(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, colorMode: Int) {
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

            imageView = getSvgImageView(mainActivity, "esempio1.svg")

            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (colorMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Multi-color"
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Mono-color"
                }
            }
            var svgString = SVGtoString("1", mainActivity)
            svgString = modifySVG(svgString, listOf("#Luisa"),listOf("fill"),listOf("#9b1d96"))
            val svgString11 = modifySVG(svgString,listOf("#link-Luisa-42"),listOf("fill"),listOf("#b52626"))
            val svgString12 = modifySVG(svgString11,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#f56c2c", "#f56c2c", "#f56c2c"))

            val svgString21 = modifySVG(svgString,
                listOf("#link-Luisa-42"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString22 = modifySVG(svgString21,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))

            saySentence("Luisa", qiContext)
            Handler(Looper.getMainLooper()).post {
                imageView.setImageBitmap(loadSVG(svgString, mainActivity))
            }

            Thread.sleep(pause)
            saySentence("si è sposata", qiContext)

            if (colorMode == 1) {

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString11, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString21, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("a 42 anni", qiContext)

            if (colorMode == 1) {

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString12, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {

                    imageView.setImageBitmap(loadSVG(svgString22, mainActivity))
                }
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }
        }

        private fun replyComplexity(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, complexity: Int) {

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

            imageView = getSvgImageView(mainActivity, "esempio7.svg")
            val svgString = SVGtoString("7", mainActivity)
            val svgString11 = modifySVG(svgString,
                listOf("#27", "#27", "#pallino-27", "#pallino-27"),
                listOf("fill", "font-size", "fill", "stroke"),
                listOf("#9b1d96","600px", "#9b1d96", "#9b1d96"))
            val svgString12 = modifySVG(svgString11,
                listOf("#link-Davide-27", "#link-Davide-27", "#link-Davide-27", "#link-Luca-27", "#link-Luca-27", "#link-Luca-27"),
                listOf("fill", "stroke", "stroke-width", "fill", "stroke", "stroke-width"),
                listOf("#b52626", "#b52626", "0.15", "#b52626", "#b52626", "15"))
            val svgString13 = modifySVG(svgString12,
                listOf("#Davide", "#Davide"),
                listOf("fill", "font-size"),
                listOf("#b57026", "600px"))
            val svgString14 = modifySVG(svgString13,
                listOf("#Luca", "#Luca"),
                listOf("font-size", "fill"),
                listOf("600px", "#b57026"))

            val svgString21 = modifySVG(svgString,
                listOf("#27", "#pallino-27", "#pallino-27"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))
            val svgString22 = modifySVG(svgString21,
                listOf("#link-Davide-27", "#link-Luca-27"),
                listOf("fill", "fill"),
                listOf("#9b1d96", "#9b1d96"))
            val svgString23 = modifySVG(svgString22,
                listOf("#Davide"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString24 = modifySVG(svgString23,
                listOf("#Luca"),
                listOf("fill"),
                listOf("#9b1d96"))


            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (complexity == 4) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Senza pausa - modifiche più evidenti"
                }

                thread {
                    Thread.sleep(740)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString11, mainActivity))
                    }

                    Thread.sleep(1100)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString12, mainActivity))
                    }

                    Thread.sleep(450)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString13, mainActivity))
                    }

                    Thread.sleep(350)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString14, mainActivity))
                    }

                }

                saySentence("A 27 anni si sono sposati Davide e Luca", qiContext)
            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Con pausa - mofiche meno evidenti"
                }

                saySentence("A 27 anni", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString21, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("si sono sposati", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString22, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("Davide", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString23, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("e Luca", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString24, mainActivity))
                }
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }

        }

        private fun replyPronounce(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, pronunciationMode: Int) {
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

            val svgString = SVGtoString("5", mainActivity)
            imageView = getSvgImageView(mainActivity, "esempio5.svg")
            val svgString1 = modifySVG(svgString,
                listOf("#Giacomo"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString2 = modifySVG(svgString1,
                listOf("#link-Giacomo-30", "#link-Giacomo-42"),
                listOf("fill", "fill"),
                listOf("#9b1d96", "#9b1d96"))
            val svgString3 = modifySVG(svgString2,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))
            val svgString4 = modifySVG(svgString3,
                listOf("#30", "#pallino-30", "#pallino-30"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))



            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (pronunciationMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Pronuncia con pause"
                }
                saySentence("L'elemento Giacomo", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString1, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("del dominio dei nomi è collegato", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString2, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("agli elementi 42", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString3, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("e 30", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString4, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("del codominio delle età", qiContext)

            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Pronuncia senza pausa"
                }
                thread {
                    Thread.sleep(1100)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString1, mainActivity))
                    }

                    Thread.sleep(1480)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString2, mainActivity))
                    }

                    Thread.sleep(920)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString3, mainActivity))
                    }

                    Thread.sleep(400)

                    Handler(Looper.getMainLooper()).post {
                        imageView.setImageBitmap(loadSVG(svgString4, mainActivity))
                    }
                }

                saySentence(
                    "L'elemento Giacomo del dominio dei nomi è collegato agli elementi 42 e 30 del codominio delle età",
                    qiContext
                )

            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }
        }

        private fun replyManyChanges(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, imageModifierMode: Int) {
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

            imageView = getSvgImageView(mainActivity, "esempio4.svg")

            val svgString = SVGtoString("4", mainActivity)
            val svgString11 = modifySVG(svgString,
                listOf("#Carlotta", "#Carlotta", "#Carlotta"),
                listOf("fill", "font-size", "fill"),
                listOf("#9b1d96", "600px", "#9b1d96"))
            val svgString12 = modifySVG(svgString11,
                listOf("#insieme-dominio"),
                listOf("fill"),
                listOf("#8ad6db"))
            val svgString13 = modifySVG(svgString12,
                listOf("#link-Carlotta-42", "#link-Carlotta-42", "#link-Carlotta-42"),
                listOf("fill", "stroke-width", "stroke"),
                listOf("#9b1d96", "0.15", "#9b1d96"))
            val svgString14 = modifySVG(svgString13,
                listOf("#42", "#42", "#pallino-42", "#pallino-42"),
                listOf("font-size", "fill", "fill", "stroke"),
                listOf("600px", "#9b1d96", "#9b1d96", "#9b1d96"))
            val svgString15 = modifySVG(svgString14,
                listOf("#insieme-codominio"),
                listOf("fill"),
                listOf("#8ad6db"))



            val svgString21 = modifySVG(svgString,
                listOf("#Carlotta"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString22 = modifySVG(svgString21,
                listOf("#insieme-dominio"),
                listOf("fill"),
                listOf("#8ad6db"))
            val svgString23 = modifySVG(svgString22,
                listOf("#link-Carlotta-42"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString24 = modifySVG(svgString23,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))
            val svgString25 = modifySVG(svgString24,
                listOf("#insieme-codominio"),
                listOf("fill"),
                listOf("#8ad6db"))


            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Con ingrandimento - Molte modifiche"
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Senza ingrandimento - Molte modifiche"
                }
            }

            saySentence("L'elemento Carlotta", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString11, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString21, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("del dominio dei nomi", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString12, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString22, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("è collegato", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString13, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString23, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("all'elemento 42", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString14, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString24, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("del codominio delle età", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString15, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString25, mainActivity))
                }
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }
        }

        private fun replyFewChanges(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, imageModifierMode: Int) {
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

            val svgString = SVGtoString("3", mainActivity)
            imageView = getSvgImageView(mainActivity, "esempio3.svg")

            val svgString11 = modifySVG(svgString,
                listOf("#Leone", "#Leone"),
                listOf("fill", "font-size"),
                listOf("#9b1d96", "600px"))
            val svgString12 = modifySVG(svgString11,
                listOf("#link-Leone-42", "#link-Leone-42", "#link-Leone-42"),
                listOf("fill", "stroke", "stroke-width"),
                listOf("#9b1d96", "#9b1d96", "0.15"))
            val svgString13 = modifySVG(svgString12,
                listOf("#42", "#42", "#pallino-42", "#pallino-42"),
                listOf("font-size", "fill", "fill", "stroke"),
                listOf("600px", "#9b1d96", "#9b1d96", "#9b1d96"))


            val svgString21 = modifySVG(svgString,
                listOf("#Leone"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString22 = modifySVG(svgString21,
                listOf("#link-Leone-42"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString23 = modifySVG(svgString22,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))



            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Con ingrandimento - Poche modifiche"
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Senza ingrandimento - Poche modifiche"
                }
            }

            saySentence("L'elemento Leone del dominio dei nomi", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString11, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString21, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("è collegato", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString12, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString22, mainActivity))
                }
            }

            Thread.sleep(pause)
            saySentence("all'elemento 42 del codominio delle età", qiContext)

            if (imageModifierMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString13, mainActivity))
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString23, mainActivity))
                }
            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }
        }

        private fun replyVoiceImageCoordination(qiContext: QiContext, layout: LinearLayout, mainActivity: MainActivity, voiceImageMode: Int) {

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

            imageView = getSvgImageView(mainActivity, "esempio2.svg")
            val svgString = SVGtoString("2", mainActivity)
            val svgString1 = modifySVG(svgString,
                listOf("#Luigina"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString2 = modifySVG(svgString1,
                listOf("#Remo"),
                listOf("fill"),
                listOf("#9b1d96"))
            val svgString3 = modifySVG(svgString2,
                listOf("#link-Luigina-42", "#link-Remo-42"),
                listOf("fill", "fill"),
                listOf("#9b1d96", "#9b1d96"))
            val svgString4 = modifySVG(svgString3,
                listOf("#42", "#pallino-42", "#pallino-42"),
                listOf("fill", "fill", "stroke"),
                listOf("#9b1d96", "#9b1d96", "#9b1d96"))


            Handler(Looper.getMainLooper()).post {

                topRow.addView(imageView)

                layout.addView(topRow)
                layout.addView(botRow)
            }

            if (voiceImageMode == 1) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Modifiche a fine frase"
                }

                saySentence("Luigina e Remo si sono sposati a 42 anni", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString4, mainActivity))
                }

            } else if (voiceImageMode == 2) {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Modifiche simultanee alle parole"
                }

                saySentence("Luigina", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString1, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("e Remo", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString2, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("si sono sposati", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString3, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("a 42 anni", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString4, mainActivity))
                }

            } else {
                Handler(Looper.getMainLooper()).post {
                    label.text = "Modifiche a blocchi di dipendenza sintattiche"
                }

                saySentence("Luigina e Remo", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString2, mainActivity))
                }

                Thread.sleep(pause)
                saySentence("si sono sposati a 42 anni", qiContext)

                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(loadSVG(svgString4, mainActivity))
                }

            }

            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }
        }


        private fun replyIntro(
            qiContext: QiContext,
            layout: LinearLayout,
            mainActivity: MainActivity
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

            var svgString = SVGtoString("1", mainActivity)
            svgString = modifySVG(svgString, listOf("#insieme-dominio"),
                listOf("fill"), listOf("#8ad6db"))
            val svgString1 = modifySVG(svgString,
                listOf("#insieme-codominio"),
                listOf("fill"),
                listOf("#8ad6db"))

            imageView = getSvgImageView(mainActivity, "esempio1.svg")

            Handler(Looper.getMainLooper()).post {
                topRow.addView(imageView)
                layout.addView(topRow)
                layout.addView(botRow)
            }

            saySentence("Tutti gli elementi del dominio", qiContext)

            Handler(Looper.getMainLooper()).post {
                imageView.setImageBitmap(loadSVG(svgString, mainActivity))
            }

            Thread.sleep(pause)
            saySentence("sono nomi di persone mentre tutti gli elementi del codominio", qiContext)
            Handler(Looper.getMainLooper()).post {
                imageView.setImageBitmap(loadSVG(svgString1, mainActivity))
            }

            Thread.sleep(pause)

            saySentence("sono le età alle quali si sono sposati.", qiContext)






            Thread.sleep(sleep)

            Handler(Looper.getMainLooper()).post {
                val paramB = LinearLayout.LayoutParams(150, 75)
                paramB.setMargins(15, 0, 0, 0)
                btnNext.layoutParams = paramB

                val paramBN = LinearLayout.LayoutParams(150, 75)
                paramBN.setMargins(955, 0, 0, 0)
                btnRepeat.layoutParams = paramBN

                botRow.addView(btnRepeat)
                botRow.addView(btnNext)
            }

        }

        private fun getSvgImageView(mainActivity: MainActivity, fileName: String): ImageView {
            val image = ImageView(mainActivity)
            image.scaleType = ImageView.ScaleType.FIT_CENTER
            val imageParams = LinearLayout.LayoutParams(1200,575).apply {
                setMargins(40, 30, 40, 20)
            }
            image.layoutParams = imageParams
            image.setImageBitmap(loadSvg(mainActivity, fileName))

            return image
        }

        private fun loadSvg(context: Context, assetFileName: String): Bitmap? {
            val width = 2480
            val height = 1250

            return try {
                val inputStream: InputStream = context.assets.open(assetFileName)
                val svgContent = inputStream.bufferedReader().use { it.readText() }

                val fixedSvgContent = svgContent.replace("auto-start-reverse", "0")
                val modifiedInputStream = ByteArrayInputStream(fixedSvgContent.toByteArray())

                val svg = SVG.getFromInputStream(modifiedInputStream)
                svg.setDocumentWidth(width.toFloat())
                svg.setDocumentHeight(height.toFloat())

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                svg.renderToCanvas(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun loadAndModifySVG(id_elements: List<String>, style_names: List<String>, style_values: List<String>, assetFileNumber: String, context: Context): Bitmap? {
            val width = 2480
            val height = 1250

            return try {
                val inputStream: InputStream = context.assets.open("esempio$assetFileNumber.svg")
                var svgContent = inputStream.bufferedReader().use { it.readText() }

                svgContent = StrUtils.replaceTagsMulti(svgContent, id_elements, style_names, style_values)

                val fixedSvgContent = svgContent.replace("auto-start-reverse", "0")
                val modifiedInputStream = ByteArrayInputStream(fixedSvgContent.toByteArray())

                val svg = SVG.getFromInputStream(modifiedInputStream)
                svg.setDocumentWidth(width.toFloat())
                svg.setDocumentHeight(height.toFloat())

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                svg.renderToCanvas(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        }

        fun loadSVG(fixedSvgContent: String, context: Context): Bitmap? {
            val width = 2480
            val height = 1250

            return try {
                val modifiedInputStream = ByteArrayInputStream(fixedSvgContent.toByteArray())

                val svg = SVG.getFromInputStream(modifiedInputStream)
                svg.setDocumentWidth(width.toFloat())
                svg.setDocumentHeight(height.toFloat())

                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                svg.renderToCanvas(canvas)
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun modifySVG(svgContent: String, id_elements: List<String>, style_names: List<String>, style_values: List<String>): String {
            return StrUtils.replaceTagsMulti(svgContent, id_elements, style_names, style_values)
        }

        fun SVGtoString(assetFileNumber: String, context: Context): String {
            return try {
                val inputStream: InputStream = context.assets.open("esempio$assetFileNumber.svg")
                val svgContent = inputStream.bufferedReader().use { it.readText() }
                val fixedSvgContent = svgContent.replace("auto-start-reverse", "0")

                fixedSvgContent
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        private fun clearPage(layout: LinearLayout) {
            layout.removeAllViews()
            label.text = ""
            layout.addView(label)
        }

        fun saySentence(s: String, qiContext: QiContext) {
            if (language == "it") {
                val locale = Locale(Language.ITALIAN, Region.ITALY)
                SayBuilder.with(qiContext)
                    .withText(s)
                    .withLocale(locale)
                    .withBodyLanguageOption(BodyLanguageOption.DISABLED)
                    .build()
                    .run()
            }
            //if (emulator) {
            //    Thread.sleep(pause)
            //}
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
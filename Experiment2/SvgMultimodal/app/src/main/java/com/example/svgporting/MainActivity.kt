package com.example.svgporting

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.HolderBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.`object`.conversation.BodyLanguageOption
import com.aldebaran.qi.sdk.`object`.holder.AutonomousAbilitiesType
import com.aldebaran.qi.sdk.`object`.holder.Holder
import com.example.svgporting.Results.Companion.q
import com.example.svgporting.Results.Companion.qf
import com.example.svgporting.Results.Companion.qm
import com.example.svgporting.Results.Companion.qp
import com.example.svgporting.Results.Companion.saveResults
//import org.intellij.lang.annotations.Language
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.concurrent.thread
import kotlin.system.exitProcess
import com.aldebaran.qi.sdk.`object`.locale.*
import com.aldebaran.qi.sdk.`object`.locale.Locale


class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    private lateinit var layout: LinearLayout
    private lateinit var label: TextView
    var questionNumber: Int = 1

    private var step = 1 // 1 - 21
    private var displayStep = 1
    private val total = 6 //  32
    private val textMargin = 300
    private val textWidth = 900
    private val sizeCheck = 26f

    private var execute = false
    private var executeQ = true
    private var question = 0

    private val textSize = 24f
    lateinit var gothamFont: Typeface
    lateinit var gothamFontBook: Typeface

    private val pages = mutableMapOf(2 to mutableListOf(1,2,3,4).shuffled(), 3 to mutableListOf(1,2,3)/*.shuffled()*/, 4 to mutableListOf(1,2).shuffled(), 5 to mutableListOf(1,2).shuffled(), 6 to mutableListOf(1,2).shuffled())
    private var strat = pages.entries.first()
    private var videos = mutableListOf(1)
    private var page = 1

    private val tablet_width = 1280
    private val tablet_height = 706

    var voce = false

    val shape = GradientDrawable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shape.cornerRadius = 20f // Imposta la curvatura degli angoli
        shape.setColor(Color.parseColor("#ADD8E6"))

        gothamFont = ResourcesCompat.getFont(this, R.font.gotham_light)!!
        gothamFontBook = ResourcesCompat.getFont(this, R.font.gotham_book)!!

        //setContentView(R.layout.activity_main)
        val linearLayout = LinearLayout(this)

        // Impostazione del layout params per far sì che il layout occuperà l'intero spazio disponibile
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL

        layout = linearLayout

        val textView = AppCompatTextView(this)
        textView.text = ""

        val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,92)
        textView.gravity = Gravity.CENTER // Centra il testo nella TextView
        textView.textSize = 24f // Imposta la dimensione del testo
        textView.setTypeface(null, Typeface.BOLD) // Imposta il testo in grassetto
        textView.setTextColor(resources.getColor(android.R.color.white))
        textView.layoutParams = textParams
        textView.setBackgroundColor(resources.getColor(android.R.color.holo_blue_light))
        layout.addView(textView)
        label = textView
        label.typeface = gothamFontBook



        setContentView(layout)

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        disableRobotMovements(qiContext)


        val filesDir = qiContext.getFilesDir()
        val resultsFolderPath = File(filesDir, "results")
        val filePath = File(resultsFolderPath, "file.csv")

        try {
            val br = BufferedReader(FileReader(filePath))
            while (br.readLine() != null) {
                questionNumber++
            }

            println("Numero di righe nel file CSV: $questionNumber")
        } catch (e: Exception) {
            println("Si è verificato un errore durante la lettura del file CSV: ${e.message}")
            questionNumber = 1
        }


        //saveResults(qiContext)

        saySentence("Ciao!", qiContext)

        // loadStartPage(qiContext)
        loadInit(qiContext)

        saySentence("In questo questionario ti chiederò di valutare varie strategie con cui modifico le immagini durante le spiegazioni. Le immagini mostreranno un insieme di persone collegate a diverse età, che indicano quando ciascuna di esse si è sposata. L’obiettivo è capire quali modalità visive rendono la spiegazione più chiara e facile da seguire.", qiContext)

        listener(qiContext)
        // createDiscuss(qiContext)
    }

    fun disableRobotMovements(qiContext: QiContext) {
        val holder: Holder = HolderBuilder.with(qiContext)
            .withAutonomousAbilities(AutonomousAbilitiesType.AUTONOMOUS_BLINKING)
            .build()

        // Hold the ability asynchronously.
        holder.async().hold()
    }

    private fun listener(qiContext: QiContext) {
        thread {
            while (true) {
                Thread.sleep(100)

                if (step == 1 && execute && videos.isNotEmpty()) {
                    loadPreface(qiContext, 1)
                    execute = false
                } else if (step <= 26) {

                    if (execute && videos.isNotEmpty()) {
                        execute = false

                        questionnaire(qiContext, page, videos.first(), layout, this)

                        loadQuestion(qiContext, "Quanto è stata efficace questa animazione?", page, videos.first())
                        videos.removeAt(0)
                    }

                    if (videos.isEmpty() && !execute && question == 1) {
                        question++

                        if (page == 1) {
                            loadQuestion(qiContext, "Quanto ritieni che le modifiche all'immagine rendano il messaggio più efficace?", page, 0)
                        } else {
                            loadChoice(qiContext, page)
                        }
                    }

                    if (videos.isEmpty() && !execute && question == 3 && pages.isNotEmpty()) {
                        strat = pages.entries.shuffled().first()
                        page = strat.key
                        videos = strat.value as MutableList<Int>
                        pages.remove(strat.key)

                        loadPreface(qiContext, page)
                    }

                    if (question == 4) {
                        execute = true
                        question = 0
                    }

                    if (question == 1 && !execute && videos.isNotEmpty()) {
                        question = 0
                        execute = true
                    }
                } else {
                    if (executeQ) {
                        when (step) {
                            //22 -> {
                                //executeQ = false
                                //loadComment(qiContext)
                            //}
                            27 -> {
                                executeQ = false
                                loadOptional(qiContext, "Quanti anni hai?", arrayOf("Meno di 18", "18 - 30", "31 - 45", "46 - 60", "Più di 60"))
                            }
                            28 -> {
                                executeQ = false
                                loadOptional(qiContext, "Qual è il tuo genere?", arrayOf("Maschio", "Femmina", "Preferisco non dichiararlo"))
                            }
                            29 -> {
                                executeQ = false
                                loadOptional(qiContext, "Che titolo di studio possiedi?", arrayOf("Licenza elementare", "Licenza media", "Diploma", "Laurea", "Post laurea"))
                            }
                            30 -> {
                                executeQ = false
                                loadOptional(qiContext, "Qual è la tua professione?", arrayOf("Libero professionista", "Dipendente", "Insegnante", "Artigiano", "Operaio", "Studente"))
                            }
                            31 -> {
                                executeQ = false
                                loadMultiCheck(qiContext, "Quali di questi dispositivi utilizzi?", arrayOf("Computer", "Smartphone", "Tablet", "Wearable (smat watch, braccialetto fitness, etc.)", "Smart speaker", "Robot"))
                            }
                            32 -> {
                                executeQ = false
                                loadOptional(qiContext, "Ti era mai capitato di interagire con un robot prima d'ora?", arrayOf("Sì", "No"))
                            }
                            33 -> {
                                executeQ = false
                                saveResults(qiContext)
                                loadThanks(qiContext)
                            }
                        }
                    }                    
                }
            }
        }
    }

    private fun loadThanks(qiContext: QiContext) {
        val textView = getTextView("\n\nGrazie per aver partecipato!\n\n\nID questionario: " + questionNumber + "\n\n")

        val button = Button(this)
        button.text = "Fine"
        button.typeface = gothamFontBook

        val paramB = LinearLayout.LayoutParams(160, 80)
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER

        saySentence("Grazie per aver partecipato!", qiContext)

        button.setOnClickListener {
            button.isEnabled = false
            exitProcess(0)
        }

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)
            layout.addView(textView)
            layout.addView(button)
        }
    }

    private fun loadMultiCheck(qiContext: QiContext, s: String, choices: Array<String>) {
        val textView = getTextView("\nDomande personali (facoltative)\n\n" + s + "\n")

        val col = LinearLayout(this)
        col.orientation = LinearLayout.VERTICAL
        val topRowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        topRowParams.gravity = Gravity.CENTER
        col.layoutParams = topRowParams
        col.gravity = Gravity.CENTER_HORIZONTAL

        for (i in 1..choices.size) {

            qm[i] = false
            val checkBox = CheckBox(this)
            checkBox.text = choices[i-1]
            checkBox.textSize = sizeCheck


            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                qm[i] = isChecked
            }

            col.addView(checkBox)
        }

        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        paramB.setMargins(0, 50, 0, 0) // Imposta la posizione
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER

        button.setOnClickListener {
            button.isEnabled = false
            step++
            executeQ = true
        }
        button.background = shape

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)

            addProgress(layout)
            layout.addView(textView)
            layout.addView(col)
            layout.addView(button)
        }

    }

    private fun loadOptional(qiContext: QiContext, s: String, options: Array<String>) {
        val textView = getTextView("\nDomande personali (facoltative)\n\n" + s + "\n")
        qp[step - 22] = " "

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        val topRowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = topRowParams
        row.gravity = Gravity.CENTER

        val radioGroup = RadioGroup(this)
        radioGroup.orientation = LinearLayout.VERTICAL

        for (i in 1..options.size) {
            val radioButton = RadioButton(this)
            radioButton.text = options[i-1]
            radioButton.textSize = sizeCheck

            radioGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Handler(Looper.getMainLooper()).post {
                        println(options[i-1])
                        qp[step - 22] = options[i-1]
                    }
                }
            }
        }

        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        paramB.setMargins(0, 50, 0, 0) // Imposta la posizione
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER
        button.background = shape

        button.setOnClickListener {
            button.isEnabled = false
            step++
            executeQ = true
        }


        Handler(Looper.getMainLooper()).post {
            clearPage(layout)

            //addProgress(layout)
            layout.addView(textView)
            row.addView(radioGroup)
            layout.addView(row)
            layout.addView(button)
        }
    }

    private fun loadComment(qiContext: QiContext) {
        val textView = getTextView("\nCommento personale (facoltativo)\n\nIn questa sezione puoi scrivere un commento relativo al sondaggio con eventuali critiche o idee.")

        val editText = EditText(this)
        val layoutParams = ConstraintLayout.LayoutParams(800, 200)
        editText.layoutParams = layoutParams

        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        paramB.setMargins(0, 50, 0, 0) // Imposta la posizione
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER
        button.background = shape

        button.setOnClickListener {
            qp[step - 22] = editText.text.toString()

            button.isEnabled = false
            step++
            executeQ = true
        }

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)

            addProgress(layout)
            layout.addView(textView)
            layout.addView(editText)
            layout.addView(button)
        }
    }

    private fun loadChoice(qiContext: QiContext, page: Int) {
        var s = ""
        var s1 = ""
        var s2 = ""
        var choices = 2
        var q1 = false
        var q2 = true

        if (page == 2) {
            s = "Scegli la modalità che hai apprezzato maggiormente: quella in cui gli elementi cambiavano dimensione o in cui non la cambiavano?"
            s1 = "Cambiavano dimensione  "
            s2 = "Non la cambiavano"
            q2 = false
        } else if (page == 3) {
            s = "Scegli la modalità che hai apprezzato maggiormente tra le tre proposte?"
            s1 = ""
            s2 = ""
            choices = 3
        } else if (page == 4) {
            s = "Scegli la modalità che hai apprezzato maggiormente: quella in cui la frase veniva pronunciata con delle pause o senza?"
            s1 = "Con pausa  "
            s2 = "Senza pausa"
        } else if (page == 5) {
            s = "Scegli la modalità che hai apprezzato maggiormente tra le due proposte"
            s1 = "Multi-color "
            s2 = " Mono-color"
        } else if (page == 6) {
            s = "Scegli quale animazione hai apprezzato maggiormente tra le due proposte"
            s1 = "Con pausa\n  modifiche meno evidenti"
            s2 = "Senza pausa\nmodifiche più evidenti "
        }

        val textView = getTextView("\n" + s + "\n\n")
        val textView1 = getTextView(s1 + " ", false)
        val textView2 = getTextView(" " + s2, false)

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        val topRowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = topRowParams
        row.gravity = Gravity.CENTER


        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook
        button.background = shape

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        if (page == 2) {
            paramB.setMargins(0, 90, 0, 0)
        } else {
            paramB.setMargins(0, 100, 0, 0)
        }
        button.layoutParams = paramB

        button.gravity = Gravity.CENTER

        button.setOnClickListener {
            question++
            step++
            //displayStep++
            button.isEnabled = false
        }

        val radioGroup = RadioGroup(this)
        radioGroup.orientation = LinearLayout.HORIZONTAL

        for (i in 1..choices) {
            val radioButton = RadioButton(this)
            radioButton.text = "   "//i.toString()
            radioButton.textSize = sizeCheck

            if (page == 3) {
                radioGroup.orientation = LinearLayout.VERTICAL
                radioButton.setTypeface(gothamFont)


                when (i) {
                    1 -> radioButton.text = "Modifiche alla fine"
                    2 -> radioButton.text = "Modifiche simultanee alle parole"
                    3 -> radioButton.text = "Modifiche in base alla struttura sintattica"
                }
            }

            radioGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    Handler(Looper.getMainLooper()).post {
                        q1 = true
                        if (q2) {
                            button.isEnabled = true
                        }

                        qf[page] = i
                    }
                }
            }
        }

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)

            addProgress(layout)

            button.isEnabled = false

            layout.addView(textView)

            row.addView(textView1)
            row.addView(radioGroup)
            row.addView(textView2)

            layout.addView(row)


            if (page == 2) {
                val row2 = LinearLayout(this)
                row2.orientation = LinearLayout.HORIZONTAL
                val topRowParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                row2.layoutParams = topRowParams
                row2.gravity = Gravity.CENTER


                val textViewb = getTextView("\nScegli la modalità che hai apprezzato maggiormente: quella con molte o poche modifiche?\n")


                val radioGroup2 = RadioGroup(this)
                radioGroup2.orientation = LinearLayout.HORIZONTAL


                for (i in 1..choices) {
                    val radioButton = RadioButton(this)
                    radioButton.setTypeface(gothamFont)
                    if (i == 1) {
                        radioButton.text = "Molte  "
                    } else {
                        radioButton.text = "  Poche"
                    }

                    radioButton.textSize = sizeCheck

                    radioGroup2.addView(radioButton)

                    radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            Handler(Looper.getMainLooper()).post {
                                q2 = true
                                if (q1) {
                                    button.isEnabled = true
                                }
                                qf[6] = i
                            }
                        }
                    }
                }

                row2.addView(radioGroup2)
                layout.addView(textViewb)
                layout.addView(row2)
                //layout.addView(radioGroup2)
                paramB.setMargins(0, 50, 0, 0)
            }

            layout.addView(button)
        }

        saySentence(s, qiContext)
    }

    private fun addProgress(layout: LinearLayout) {
        val p = TextView(this)
        p.text = "Sezione: " + (displayStep - 1) + " / " + total + "  "
        p.textSize = 16f
        p.setTextColor(resources.getColor(android.R.color.black))
        val textParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textParams.addRule(RelativeLayout.ALIGN_RIGHT)
        p.layoutParams = textParams
        p.gravity = Gravity.RIGHT

        layout.addView(p)
    }

    private fun loadPreface(qiContext: QiContext, page: Int) {

        var s = getPhase(displayStep) + " sezione:\n"
        if (page == 1) {
            s += "Valuta l'efficacia di una spiegazione vocale coordinata a delle modifiche all'immagine sullo schermo.\n\n"
        } else if (page == 2) {
            s +=  "Valuta le modalità di presentazione considerando la tipologia e la quantità di modifiche, prima singolarmente e poi complessivamente.\n\nTi mostrerò diverse opzioni di comunicazione, queste includono la modifica di molte o poche parti dell'immagine ed in cui le dimensioni degli elementi possono variare o rimanere fissi."
        } else if (page == 3) {
            s += "Valuta le seguenti modalità di coordinazione delle modifiche all'immagine con la voce che preferisci tra le tre proposte, prima singolarmente e poi complessivamente.\n\nLe modifiche possono comparire alla fine della frase, contemporaneamente alle parole, oppure la loro comparsa può dipendere dalla struttura sintattica della frase.\n"
        } else if (page == 4) {
            s += "Valuta la modalità con cui preferisci che io parli tra le due proposte, prima singolarmente e poi complessivamente.\n\nPosso parlare facendo o meno una pausa quando modifico l'immagine\n"
        } else if (page == 5) {
            s += "Valuta la modalità di presentazione delle modifiche, con più colori o con uno solo.\n"
        } else if (page == 6) {
            s += "Valuta l'efficacia comunicativa delle due seguenti animazioni, prima singolarmente e poi complessivamente.\n"
        }

        val textView = getTextView("\n" + s + "\n\n")


        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook
        button.background = shape

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        paramB.setMargins(0, 0, 0, 0) // Imposta la posizione
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER

        button.setOnClickListener {
            button.isEnabled = false
            question++
            step++
        }

        thread {
            Handler(Looper.getMainLooper()).post {
                clearPage(layout)

                addProgress(layout)

                layout.addView(textView)
            }

            Thread.sleep(1000)

            Handler(Looper.getMainLooper()).post {
                layout.addView(button)
            }
        }

        saySentence(pronounceReplace(s), qiContext)
    }

    private fun getPhase(ds: Int): String {
        displayStep++
        return when (ds) {
            1 -> "Prima"
            2 -> "Seconda"
            3 -> "Terza"
            4 -> "Quarta"
            5 -> "Quinta"
            6 -> "Sesta"
            else -> ""
        }
    }

    private fun pronounceReplace(s: String): String {
        return s.replace("Valuta", "Vàluta")
    }

    private fun questionnaire(qiContext: QiContext, page: Int, video: Int, layout: LinearLayout, mainActivity: MainActivity) {
        if (page == 1) {
            SentenceReplier.replyTo(qiContext,1,1,1,1,1,0,layout,mainActivity)
        } else if (page == 2) {
            if (video == 1) {
                SentenceReplier.replyTo(qiContext, 2, 1, 1, 2, 2,0, layout, mainActivity)
            } else if (video == 2) {
                SentenceReplier.replyTo(qiContext, 2, 1, 1, 1, 2,0, layout, mainActivity)
            } else if (video == 3) {
                SentenceReplier.replyTo(qiContext, 2, 1, 1, 2, 1,0, layout, mainActivity)
            } else if (video == 4) {
                SentenceReplier.replyTo(qiContext, 2, 1, 1, 1, 1,0, layout, mainActivity)
            }

        } else if (page == 3) {
            if (video == 1) {
                SentenceReplier.replyTo(qiContext, 3, 1, 0, 0, 0,0, layout, mainActivity)
            } else if (video == 2) {
                SentenceReplier.replyTo(qiContext, 3, 2, 0, 0, 0,0, layout, mainActivity)
            } else if (video == 3) {
                SentenceReplier.replyTo(qiContext, 3, 3, 0, 0, 0,0, layout, mainActivity)
            }
        } else if (page == 4) {
            if (video == 1) {
                SentenceReplier.replyTo(qiContext, 4, 1, 1, 1, 1,0, layout, mainActivity)
            } else if (video == 2) {
                SentenceReplier.replyTo(qiContext, 4, 1, 2, 1, 1,0, layout, mainActivity)
            }
        } else if (page == 5) {
            if (video == 1) {
                SentenceReplier.replyTo(qiContext, 5, 1, 1, 1, 1,1, layout, mainActivity)
            } else if (video == 2) {
                SentenceReplier.replyTo(qiContext, 5, 2, 2, 2, 2,2, layout, mainActivity)
            }
        } else if (page == 6) {
            if (video == 1) {
                SentenceReplier.replyTo(qiContext, 6, 1, 1, 1, 1,0, layout, mainActivity)
            } else if (video == 2) {
                SentenceReplier.replyTo(qiContext, 6, 2, 2, 2, 2,0, layout, mainActivity)
            }
        }
    }

    private fun loadQuestion(
        qiContext: QiContext,
        s: String,
        page: Int,
        strat: Int
    ) {

        val textView = getTextView("\n\n" + s + "\n\n")

        val textView1 = getTextView("Per niente ", false)

        val textView2 = getTextView(" Molto", false)

        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        val topRowParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = topRowParams
        row.gravity = Gravity.CENTER_HORIZONTAL


        val radioGroup = RadioGroup(this)
        radioGroup.orientation = LinearLayout.HORIZONTAL

        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook
        button.background = shape

        val paramB = LinearLayout.LayoutParams(160, 80) // Imposta le dimensioni
        paramB.setMargins(0, 100, 0, 0) // Imposta la posizione
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER

        button.setOnClickListener {
            button.isEnabled = false
            question++
            //displayStep++
            step++
        }

        for (i in 1..7) {
            val radioButton = RadioButton(this)
            radioButton.text = i.toString() + "   "
            radioButton.textSize = sizeCheck

            radioGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (strat == 0) {
                        qf[1] = i
                    } else {
                        val key = Pair(page, strat)
                        q[key] = i
                    }

                    Handler(Looper.getMainLooper()).post {
                        button.isEnabled = true
                    }
                }
            }
        }

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)

            addProgress(layout)
            button.isEnabled = false

            layout.addView(textView)

            row.addView(textView1)
            row.addView(radioGroup)
            row.addView(textView2)

            layout.addView(row)
            layout.addView(button)
        }

        saySentence(s, qiContext)
    }

    private fun loadInit(qiContext: QiContext) {
        Handler(Looper.getMainLooper()).post {
            //layout = findViewById(R.id.container)
            layout.gravity = Gravity.CENTER_HORIZONTAL
        }

        val textView = getTextView("\nCiao!\nIn questo questionario ti chiederò di valutare varie strategie con cui modifico le immagini durante le spiegazioni.\n\nLe immagini mostreranno un insieme di persone collegate a diverse età, che indicano quando ciascuna di esse si è sposata.\n\nL’obiettivo è capire quali modalità visive rendono la spiegazione più chiara e facile da seguire.\n")

        val button = Button(this)
        button.text = "Avanti"
        button.typeface = gothamFontBook
        button.background = shape

        val paramB = LinearLayout.LayoutParams(160, 80)
        button.layoutParams = paramB
        button.gravity = Gravity.CENTER


        button.setOnClickListener {
            button.isEnabled = false
            execute = true
        }

        Handler(Looper.getMainLooper()).post {
            clearPage(layout)
            layout.addView(textView)
            layout.addView(button)
        }
    }

    private fun clearPage(layout: LinearLayout) {
        layout.removeAllViews()
        label.text = ""
        layout.addView(label)

    }

    private fun getTextView(s: String, params: Boolean = true): TextView {
        val textView = TextView(this)
        textView.text = s
        textView.textSize = textSize
        textView.setTextColor(resources.getColor(android.R.color.black))
        val textParams = RelativeLayout.LayoutParams(
            textWidth,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        textView.setLineSpacing(8f, 1.2f)
        textView.typeface = gothamFont
        textParams.setMargins(textMargin, 0, 0, 0)
        textParams.addRule(RelativeLayout.CENTER_VERTICAL)
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        if (params) {
            textView.layoutParams = textParams
        }
        textView.gravity = Gravity.CENTER

        return textView
    }

    fun saySentence(s: String, qiContext: QiContext) {
        var locale = Locale(Language.ITALIAN, Region.ITALY)
        SayBuilder.with(qiContext).withText(s).withLocale(locale).withBodyLanguageOption(BodyLanguageOption.DISABLED).build().run()
    }


    override fun onRobotFocusLost() {}
    override fun onRobotFocusRefused(reason: String) {}
}
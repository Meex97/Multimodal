package com.example.svgporting;

import com.aldebaran.qi.sdk.QiContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Logger


class Results {
    companion object {
        val q: MutableMap<Pair<Int,Int>, Int> = mutableMapOf()
        val qf: MutableMap<Int, Int> = mutableMapOf()
        val qp: MutableMap<Int, String> = mutableMapOf()
        val qm: MutableMap<Int, Boolean> = mutableMapOf()

        lateinit var qiContext: QiContext


        fun saveResults(qi: QiContext) {
            println(q)
            println(qf)
            println(qp)
            println(qm)

            val question = q.toList()
                .sortedWith(compareBy(
                    { it.first.first },   // Ordina per il primo elemento della chiave
                    { it.first.second }   // Poi ordina per il secondo elemento della chiave
                ))
                .toMap()

            val questionFinal = qf.toList()
                .sortedWith(compareBy { it.first })
                .toMap()

            val questionPersonal = qp.toList()
                .sortedWith(compareBy { it.first })
                .toMap()

            val questionMulti = qm.toList()
                .sortedWith(compareBy { it.first })
                .toMap()



            qiContext = qi

            val filesDir = qiContext.getFilesDir()
            val resultsFolderPath = File(filesDir, "results")
            val filePath = File(resultsFolderPath, "file.csv")



            // Dati da scrivere nel file CSV
                val header = arrayOf("sep=,\nVideo introduttivo - Questo video è molto piacevole",
                    "Video introduttivo - Ritengo che le immagini rendano la comunicazione più efficacie",
                    "Strategia 1 per la dimensione (Poche modifiche): Senza ingrandimento",
                    "Strategia 1 per la dimensione (Molte modifiche): Senza ingrandimento",
                    "Strategia 2 per la dimensione (Poche modifiche): Con ingrandimento",
                    "Strategia 2 per la dimensione (Molte modifiche): Con ingrandimento",
                    "Quale strategia per la quantità di modifiche hai preferito?",
                    "Quale strategia per la dimensione hai preferito?",
                    "Strategia 1 per le immagini: Modifiche dopo il completamento della frase",
                    "Strategia 2 per le immagini: Modifiche simultaneamente alle parole",
                    "Strategia 3 per le immagini: Modifiche dipendenti dell'albero a dipendenze",
                    "Quale strategia per l'apparizione hai preferito?",
                    "Strategia 1 per la pronuncia: Con pausa",
                    "Strategia 2 per la pronuncia: Senza pausa",
                    "Quale strategia per la pronuncia hai preferito?",
                    "Strategia 1 per i colori: multi-color",
                    "Strategia 2 per i colori: mono-color",
                    "Quale strategia per i colori hai preferito?",
                    "Strategie più sofisticate",
                    "Strategie più semplici",
                    "Che video hai preferito?",
                    "Quanti anni hai?",
                    "Qual è il tuo genere?",
                    "Che titolo di studio possiedi?",
                    "Qual è la tua professione?",
                    "Ti è mai capitato di interagire con un robot?",
                    "Computer",
                    "Smartphone",
                    "Tablet",
                    "Wearable (smat watch - braccialetto fitness - etc.)",
                    "Smart speaker (Alexa - etc.)",
                    "Robot")

            val dataRow = mutableListOf<String>()

            for (page in 1..6) {

                val filteredEntries = question.entries.filter { it.key.first == page }
                for ((_, value) in filteredEntries) {
                    dataRow.add(value.toString())
                }

                if (page == 2) {
                    dataRow.add(questionFinal[6].toString())
                }
                dataRow.add(questionFinal[page].toString())
            }

            for ((_, value) in questionPersonal) {
                dataRow.add(value)
            }

            for ((_, value) in questionMulti) {
                dataRow.add(value.toString())
            }

            // Scrivi nel file CSV
            if (!filePath.isFile) {
                writeCsvFile(filePath, header)
            }
            writeCsvFile(filePath, dataRow.toTypedArray())
        }

        private fun writeCsvFile(file: File, data: Array<String>) {

            try {
                // val file = File(filePath)
                file.parentFile?.mkdirs()

                val fileWriter = FileWriter(file, true)

                // Se il file è appena stato creato, aggiungi l'intestazione
                /*if (file.length() == 0L) {
                    val header = arrayOf("Colonna1", "Colonna2", "Colonna3")
                    fileWriter.append(header.joinToString(","))
                    fileWriter.append("\n")
                }*/

                // Aggiungi dati
                fileWriter.append(data.joinToString(","))
                println(data.joinToString(","))
                fileWriter.append("\n")
                /*fileWriter.append("6,7,6,7,7,3,1,1,1,4,3,2,6,4,1,6,7,2,18 - 30,Femmina,Laurea,Studente,Sì,true,true,true,false,false,false\n" +
                        "7,7,6,2,7,4,1,1,1,5,4,2,6,5,1,4,5,2,18 - 30,Maschio,Laurea,Studente,No,true,true,true,false,true,false\n" +
                        "6,7,7,4,7,7,1,1,7,7,7,2,7,7,1,7,7,1,18 - 30,Maschio,Laurea,Studente,No,true,true,false,false,false,false\n" +
                        "4,6,6,4,5,2,1,2,1,6,3,2,6,2,1,4,5,2,18 - 30,Maschio,Laurea,Studente,No,true,true,false,true,false,false\n" +
                        "5,6,5,4,6,4,1,1,4,5,3,2,5,4,1,5,4,2,18 - 30,Maschio,Post laurea,Studente,No,true,true,true,false,false,false\n" +
                        "3,6,7,5,7,3,1,1,1,4,4,2,3,6,2,2,4,2,18 - 30,Femmina,Laurea,Studente,Sì,true,true,true,false,false,false\n")
                */
                fileWriter.close()

                println(file.absolutePath)
                println("File CSV scritto con successo.")
            } catch (e: IOException) {
                println("Errore nella scrittura del file CSV: $e")
            }
        }

    }
}
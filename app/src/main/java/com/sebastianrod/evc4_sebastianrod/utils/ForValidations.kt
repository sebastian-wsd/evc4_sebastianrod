package com.sebastianrod.evc4_sebastianrod.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible

class ForValidations {
    companion object {

        private var customMessage = ""

        fun removeBlanks(text:String):String{
            return text.replace("\\s{2,}".toRegex(), " ") //Eliminando los intermedios
                .replace("(^\\s|\\s$)".toRegex(), "") //Eliminando los iniciales y finales
        }

        fun valOnlyText(text:String):Boolean{
            val regOnlyString = Regex("^[a-zA-Záéíóú ñ]+$")
            customMessage = "This field only contains letters"
            return regOnlyString.containsMatchIn(text)
        }

        fun valOnlyPhone(text:String):Boolean{
            customMessage = "The phone can only have 9 numbers"
            return valNumber(text, 9)
        }

        fun valOnlyRuc(text:String):Boolean{
            customMessage = "The ruc can only have 13 numbers"
            return valNumber(text, 13)
        }

        fun valOnlyPassword(text:String):Boolean{
            val regOnlyPassword = Regex("^[\\w.#]{8,}$")
            customMessage = "Password must be at least 8 characters."
            return regOnlyPassword.containsMatchIn(text)
        }

        fun valDecimal(text:String):Boolean{
            val regOnlyDecimal:Regex = Regex("^[0-9]+(\\.[0-9]+|\\.)?\$")
            customMessage = "This field only allows decimals"
            return regOnlyDecimal.containsMatchIn(text)
        }

        fun valOnlyEmail(text: String):Boolean{
            val regOnlyEmail = Regex("^[\\wáéíóú ñ\\.]+@gmail.com$")
            customMessage = "The email entered is not correct (@gmail.com)"
            return regOnlyEmail.containsMatchIn(text.toLowerCase())
        }

        fun valAllTypeText(text:String):Boolean{
            val regTextAndNumbers = Regex("^.+$")
            return regTextAndNumbers.containsMatchIn(text)
        }

        fun valNumber(text:String, numberOfDigits:Int = 0):Boolean{
            val regOnlyNumber:Regex = if (numberOfDigits == 0) Regex("^[0-9]+$") else Regex("^[0-9]{$numberOfDigits}$")
            return regOnlyNumber.containsMatchIn(text)
        }

        fun valEmptyText(text: String):Boolean{
            val regEmptyText = Regex("^\\s+\$")
            return regEmptyText.containsMatchIn(text) || text == ""
        }

        fun valInput(input:EditText, errorMessage:TextView, valFunction: (text:String) -> Boolean, numberOfDigits: Int = 0): Boolean {
            val text = removeBlanks(input.text.toString())
            var existError = true
            var finalMessage = ""

            if (valEmptyText(text)){
                finalMessage = "You must complete the field"
            } else if (!valFunction(text)){
                finalMessage = customMessage
            } else {
                existError = false
            }

            errorMessage.text = finalMessage
            errorMessage.isVisible = existError
            return existError
        }
    }
}
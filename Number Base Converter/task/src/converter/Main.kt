package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun main() {

    while (true) {
        println("Enter two numbers in format: {source base} {target base} (To quit type /exit)  ")
        val userInput = readln()
        if (userInput == "/exit") {
            break
        }
        val sourceBase = userInput.substringBefore(' ').toInt()
        val targetBase = userInput.substringAfter(' ').toInt()

        convertBases(sourceBase, targetBase)

    }

}

fun promptUser(message: String): String {
    print(message)

    return readln()
}

fun convertBases(sourceBase: Int, targetBase: Int) {
    while (true) {
        val userCommand = promptUser("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
        if (userCommand == "/back") {
            return
        }

        var number = userCommand
        if (sourceBase == targetBase) {
            println("Conversion result: $number")
            continue
        }

        var result = ""
        var integerPart = ""
        var fractionPart = ""
        if (number.contains('.'))
        {
            integerPart = number.substringBefore('.')
            fractionPart = ("." + number.substringAfter('.'))

            integerPart = convertToDecimal(integerPart, sourceBase)
            integerPart = convertFromDecimal(integerPart, targetBase)

            fractionPart = convertFractionToDecimal(fractionPart, sourceBase)
            fractionPart = convertFractionFromDecimal(fractionPart, targetBase)

            result = integerPart + fractionPart
        }

        else {
            result = convertToDecimal(number, sourceBase)
            result = convertFromDecimal(result, targetBase)
        }

        println("Conversion result: $result")
    }
}

fun convertFractionToDecimal(number: String, sourceBase: Int): String {
    var result = BigDecimal.ZERO
    var exponent = 1
    val list = generateListOfDigits()

    for (element in number.substringAfter('.')) {
        var nextDigit = list.indexOf(element).toBigDecimal().divide(sourceBase.toBigDecimal().pow(exponent), 5, RoundingMode.HALF_UP)
        result =  result.add(nextDigit)
        exponent++
    }

    return result.toString()
}

fun convertFractionFromDecimal(number: String, targetBase: Int): String {
    var result = "."
    val list = generateListOfDigits()
    var num = number.toBigDecimal()
    var counter = 0

    while (num > BigDecimal.ZERO && counter < 5) {
        num = num.multiply(targetBase.toBigDecimal())
        var index = num.setScale(0, RoundingMode.DOWN).toInt()
        var nextDigit = list[index]
        result += nextDigit
        counter++
        num = num.remainder(BigDecimal.ONE)
    }

    return (result + "0000000").substring(0, 6)
}

fun convertToDecimal(number: String, sourceBase: Int): String {
    var result = BigInteger.ZERO
    var exponent = 0
    val list = generateListOfDigits()

    for (element in number.reversed()) {
        val nextValue: BigInteger = (list.indexOf(element)).toBigInteger().multiply(sourceBase.toBigInteger().pow(exponent))
        exponent++
        result += nextValue
    }

    return result.toString()
}

fun convertFromDecimal(number: String, targetBase: Int): String {
    var result = ""
    var num = number.toBigInteger()
    val list = generateListOfDigits()

    while (num > BigInteger.ZERO) {
        val nextDigit = num.remainder(targetBase.toBigInteger()).toInt()
        val nextChar = list[nextDigit]
        result += nextChar
        num = num.divide(targetBase.toBigInteger())
    }

    return result.reversed()
}



fun generateListOfDigits(): String {
    var str = ""
    for (ch in '0'..'9') {
        str += ch
    }
    for (ch in 'a'.. 'z') {
        str += ch
    }

    return str
}
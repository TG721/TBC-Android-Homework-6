package com.example.tbc_homework_6

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

private class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val email: String
)

private  fun isValidEmail(email: String):Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

//validate first name and lastname
fun String.onlyLetters() = all { it.isLetter() }
private  fun isValidName(name: String):Boolean {
    if (name.onlyLetters())  return true
    return false

}

private  fun isValidAge(age: String):Boolean {
    if(age.toDoubleOrNull() == null) return false
    val ageAsDouble=age.toDouble()
    if (ageAsDouble.toInt().toDouble() != ageAsDouble  || age.toInt()<1 )  return false

    return true

}

private fun validateID(id:String, users: List<User>):Boolean{
    for (s in users) {
        if (s.id == id.toInt()) return false
    }
    return true
}



class MainActivity : AppCompatActivity() {

    var ActiveCounter =0
    var RemovecCounter =0

//private val a = User(1,"Tengo", "Gachechiladze", 5, "asd")
private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var id:EditText = findViewById<EditText>(R.id.id)
        var email:EditText = findViewById<EditText>(R.id.email)
        var age:EditText = findViewById<EditText>(R.id.age)
        var firstName:EditText = findViewById<EditText>(R.id.firstName)
        var lastName:EditText = findViewById<EditText>(R.id.lastName)

        findViewById<TextView>(R.id.activeUsers).text="Active users "+users.size
        findViewById<TextView>(R.id.removedUsers).text="Removed users "+" "+RemovecCounter

        //handling add button click
        val addButton = (findViewById<Button>(R.id.addButton))
        addButton.setOnClickListener {

            //check if all fields are filled
            if(email.text.toString().isEmpty() || age.text.toString().isEmpty() || id.text.toString().isEmpty()
                || firstName.text.toString().isEmpty() || lastName.text.toString().isEmpty()){
                var status = (findViewById<TextView>(R.id.operationStatus))
                status.text="Status: Failed, fill all fields"
                status.setTextColor(Color.RED)
            }
            //validate
            else if (!isValidEmail(email.text.toString()))
                    email.setError( "Email is not valid")

            else    if (!isValidAge(age.text.toString()))
                    age.setError("Age should be positive whole number")

            else    if(!isValidName(firstName.text.toString()) ) firstName.setError("not valid First Name")
            else    if(!isValidName(lastName.text.toString()) ) lastName.setError("not valid Last Name")

            //check if id is valid,
            //since both age and id are whole positive numbers
            //we will reuse isValidAge function
            else if(!isValidAge(id.text.toString()))  id.setError("ID must be whole positive number")

            //check if id is already taken
            else if(!validateID(id.text.toString(),users)){
                id.setError("ID is already taken, use different one")
            }

            //process insertion
            else{
                users.add(User(id.text.toString().toInt(), firstName.text.toString(), lastName.text.toString(), age.text.toString().toInt(), email.text.toString()))
                var status = (findViewById<TextView>(R.id.operationStatus))
                status.text="Status: Successful"
                status.setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.activeUsers).text="Active users "+users.size
            }

            //handling remove button click
            val removeButton = (findViewById<Button>(R.id.removeButton))
            removeButton.setOnClickListener {

                //since ID determines the user
                //we clear rest input fields
                email.text.clear()
                firstName.text.clear()
                lastName.text.clear()
                age.text.clear()

                var theOne:User? = null
                var id_int:Int = id.text.toString().toInt()
                for(s in users){
                    if (s.id == id_int) theOne=s
                }

                var status = (findViewById<TextView>(R.id.operationStatus))
                //removing the use failed
                if(theOne==null) {

                    status.text="failed, no user with such ID exists"
                    status.setTextColor(Color.RED)

                }
                //user can be removed
                else {
                    users.remove(theOne)
                    RemovecCounter++
                    status.text="Status: Successful"
                    status.setTextColor(Color.GREEN)
                    findViewById<TextView>(R.id.activeUsers).text="Active users "+users.size
                    findViewById<TextView>(R.id.removedUsers).text="Removed users "+" "+RemovecCounter
                }
            }




        }
    }
}
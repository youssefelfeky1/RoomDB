package com.example.roomdb

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.widget.Toast
import com.example.roomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDb:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb=AppDatabase.getDataBase(this)

        binding.writeBtn.setOnClickListener {
            writeData()
        }

        binding.readBtn.setOnClickListener {
            readData()
        }

        binding.deleteBtn.setOnClickListener {
            deleteAll()
        }

        binding.updateBtn.setOnClickListener {
            updateData()
        }


    }

    @SuppressLint("SetTextI18n")
    private suspend fun displayData(student: Student){

        withContext(Dispatchers.Main){
            binding.firstNameTxtview.text="First Name: " + student.firstName
            binding.lastNameTxtview.text="Last Name: " + student.lastName
            binding.rollNoTxtview.text="RollNo.: "+ student.rollNo.toString()

        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateData(){

        val firstName = binding.firstNameEdt.text.toString()
        val lastName = binding.lastNameEdt.text.toString()
        val rollNo = binding.rollNoEdt1.text.toString()
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().update(firstName, lastName, rollNo.toInt())
            }
            binding.firstNameEdt.text.clear()
            binding.lastNameEdt.text.clear()
            binding.rollNoEdt1.text.clear()
            Toast.makeText(this@MainActivity,"Successfully Updated",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }



    }

    @OptIn(DelicateCoroutinesApi::class)
    private  fun deleteAll(){
        GlobalScope.launch{
            appDb.studentDao().deleteAll()
        }
    }




    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData(){
        val firstName = binding.firstNameEdt.text.toString()
        val lastName = binding.lastNameEdt.text.toString()
        val rollNo = binding.rollNoEdt1.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty())
        {
            val student = Student(null,firstName,lastName, rollNo.toInt())

            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().insert(student)
            }
            binding.firstNameEdt.text.clear()
            binding.lastNameEdt.text.clear()
            binding.rollNoEdt1.text.clear()

            Toast.makeText(this@MainActivity,"Successfully Written",Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }


    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun readData(){

        val rollNo = binding.rollNoEdt2.text.toString()
        if(rollNo.isNotEmpty()){
            lateinit var student: Student
            GlobalScope.launch{
                student=  appDb.studentDao().findByRoll(rollNo.toInt())
                displayData(student)

            }


        }
        else{
            Toast.makeText(this@MainActivity,"Please RollNo.",Toast.LENGTH_SHORT).show()
            binding.firstNameTxtview.text=null
            binding.lastNameTxtview.text=null
            binding.rollNoTxtview.text=null
        }

    }



}
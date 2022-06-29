package com.example.mubsehealth.model

class Student{
    var id:String ?= null
    var firstName:String ?= null
    var lastName:String ?= null
    var email:String ?= null
    var course:String ?= null
    var stdNo:String ?= null
    var password:String ?= null
    var image:String ?= null
            constructor(id:String,
                        firstName:String,
                        lastName:String,
                        email:String,
                        course:String,
                        stdNo:String,
                        password:String,
                        image:String,){

                this.id = id
                this.firstName=firstName
                this.lastName=lastName
                this.email=email
                this.course=course
                this.stdNo=stdNo
                this.password=password
                this.image = image
            }

    constructor()
}

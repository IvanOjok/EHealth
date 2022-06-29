package com.example.mubsehealth.model

class Doctor {
    var id:String ?= null
    var firstName:String ?= null
    var lastName:String ?= null
    var email:String ?= null
    var profession:String ?= null
    var doctorNo:String ?= null
    var imgUrl:String?= null
    var phone:String?= null
    constructor(id:String,
                firstName:String,
                lastName:String,
                email:String,
                profession:String,
                doctorNo:String,
                imgUrl:String,
                phone:String,
               ){

        this.id = id
        this.firstName=firstName
        this.lastName=lastName
        this.email=email
        this.profession=profession
        this.doctorNo=doctorNo
        this.imgUrl = imgUrl
        this.phone = phone
    }

    constructor()
}

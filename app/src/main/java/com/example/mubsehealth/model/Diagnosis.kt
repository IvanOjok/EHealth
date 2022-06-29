package com.example.mubsehealth.model

class Diagnosis {
    var result:String ?= null
    var recommendation:String ?= null
    var prescription:String ?= null
    var dName:String ?= null
    var dId:String ?= null
    var sNo:String ?= null
    constructor(
        result:String,
        recommendation:String,
        prescription:String,
        dName:String,
        dPhone:String,
        sNo:String
    ){
        this.result=result
        this.recommendation=recommendation
        this.prescription=prescription
        this.dName = dName
        this.dId = dPhone
        this.sNo = sNo
    }

    constructor()
}
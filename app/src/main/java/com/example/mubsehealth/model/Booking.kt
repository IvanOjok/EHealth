package com.example.mubsehealth.model

class Booking {
    var date:String ?= null
    var time:String ?= null
    var purpose:String ?= null
    var dName:String ?= null
    var dId:String ?= null
    var sNo:String ?= null
    var approval:String ?= null
    var message:String ?= null
    constructor(
                date:String,
                time:String,
                purpose:String,
                dName:String,
                dPhone:String,
                sNo:String,
                approval:String,
                message:String,
                ){
        this.date=date
        this.time=time
        this.purpose=purpose
        this.dName = dName
        this.dId = dPhone
        this.sNo = sNo
        this.approval = approval
        this.message = message
    }

    constructor()
}
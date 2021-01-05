package com.droidinsight.catalog.listener


interface HomeListener {
    fun callingFunction(){

    }
    fun callingSecondFunction(){

    }

    fun passString(id: String?) {
    }


    fun passStringIntPosition(id: String?, key: Int?, position: Int)
    {

    }

    fun passTwoString(_id: String?, s: String?, position: Int) {

    }

    fun passStringPosition(_id: String?, position: Int) {

    }

//    fun passStringPositionId(
//        toString: String,
//        position: Int,
//        toString1: String
//    )


    fun onCancel(){}
    fun onOk(){}
    fun onUp(){}

    fun passStringPositionId(idString: String?, position: Int?, id: String?) {

    }

    fun passTimeSlot(_id: String?, startTime: String?, endTime: String?) {

    }




}
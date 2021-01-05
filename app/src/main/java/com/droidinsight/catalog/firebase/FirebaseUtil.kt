package com.droidinsight.catalog.firebase

import android.util.Log
import com.google.firebase.database.*

object FirebaseUtil {

    private const val KEY_UNREAD_COUNT = "unread_count"
    private const val KEY_DELETED_BY = "deleted_by"
    private const val KEY_BLOCKED_USER_BY = "blocked_by"
    const val BLOCKED_USER = "user_blocked"
    private const val KEY_CLEAR_CHAT_TIMESTAMP = "clear_chat_timestamp"


    private val databaseReference = FirebaseDatabase.getInstance().reference
    private val recentChatReference =
        databaseReference.child(DROID_INSIGHT).child(REF_RECENT_CHATS)
    private val messageReference = databaseReference.child(DROID_INSIGHT).child(REF_MESSAGES)

//    fun resetUnreadCount(senderID: String, receiverID: String) {
//        recentChatReference.child(senderID).child(receiverID).child(KEY_UNREAD_COUNT).setValue(0)
//    }


    fun resetUnreadCount(userId: String, friendId: String) {
        recentChatReference.child(userId).child(friendId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(userId).child(friendId)
                            .child(KEY_UNREAD_COUNT)
                            .setValue(0)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }


    fun incrementUnreadMessageCountOfFriend(senderID: String, receiverID: String) {
        val myUnreadCountRef = recentChatReference.child(receiverID).child(senderID).child(
            KEY_UNREAD_COUNT
        )
        myUnreadCountRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(p0: MutableData): Transaction.Result {
                if (p0.value == null) {
                    p0.value = 1
                } else {
                    p0.value = p0.value.toString().toInt() + 1
                }
                Log.e("###", "Value ---> $p0")
                return Transaction.success(p0)
            }

            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                if (p0 != null) {
                    Log.e("###", "Firebase counter increment failed.")
                } else {
                    Log.e("###", "Firebase counter increment succeeded.")
                }
            }
        })
    }

  /*  fun deleteMessage(
        userId: String,
        friendId: String,
        currentMessage: ChatMessageBeam,
        previousMessage: ChatMessageBeam
    ) {
        currentMessage.chat_room_id?.let {
            currentMessage.message_id?.let { it1 ->
                messageReference.child(it)
                    .child(it1)
                    .child(KEY_DELETED_BY).runTransaction(object : Transaction.Handler {
                        override fun doTransaction(p0: MutableData): Transaction.Result {
                            if (p0.value == null) {
                                p0.value = arrayListOf(userId)
                            } else {
                                p0.value = (p0.value as ArrayList<String>).add(userId)
                            }

                            Log.e("###", "Value ---> $p0")

                            if (previousMessage != null) {
                                updateRecentChatAfterDeleteMessage(
                                    userId,
                                    friendId,
                                    currentMessage,
                                    previousMessage
                                )
                            }
                            return Transaction.success(p0)
                        }

                        override fun onComplete(
                            p0: DatabaseError?,
                            p1: Boolean,
                            p2: DataSnapshot?
                        ) {
                            if (p0 != null) {
                                Log.e("###", "Firebase deleted by transaction failed.")
                            } else {
                                Log.e("###", "Firebase deleted by transaction succeeded.")
                            }
                        }
                    })
            }
        }
    }

    fun updateRecentChatAfterDeleteMessage(
        userId: String,
        friendId: String,
        currentMessage: ChatMessageBeam,
        previousMessage: ChatMessageBeam
    ) {
        recentChatReference.child(userId)
            .child(friendId)
            .runTransaction(object : Transaction.Handler {
                override fun doTransaction(p0: MutableData): Transaction.Result {
                    if (p0.value != null) {
                        p0.getValue(RecentChatBeam::class.java)?.let { recentChatData ->
                            if (recentChatData.timestamp == currentMessage.timestamp) {
//                                recentChatData.timestamp = previousMessage.timestamp
                                recentChatData.message = "Message Deleted"
//                                recentChatData.message_type = previousMessage.message_type
//                                recentChatData.sender_id = previousMessage.sender_id.toString()
//                                recentChatData.senderProfileImage = previousMessage.senderProfileImage.toString()
                            }
                            p0.value = recentChatData
                        }
                    }
                    Log.e("###", "Value ---> $p0")
                    return Transaction.success(p0)
                }

                override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                    if (p0 != null) {
                        Log.e("###", "Firebase recent chat transaction failed.")
                    } else {
                        Log.e("###", "Firebase recent chat transaction succeeded.")
                    }
                }
            })
    }

    fun deleteMessageForEveryOne(
        userId: String,
        friendId: String,
        currentMessage: ChatMessageBeam,
        previousMessage: ChatMessageBeam
    ) {
        currentMessage.chat_room_id?.let {
            currentMessage.message_id?.let { it1 ->
                messageReference.child(it)
                    .child(it1)
                    .child(KEY_DELETED_BY).runTransaction(object : Transaction.Handler {
                        override fun doTransaction(p0: MutableData): Transaction.Result {
                            if (p0.value == null) {
                                p0.value = arrayListOf(friendId)
                            } else {
                                p0.value = (p0.value as ArrayList<String>).add(friendId)
                            }

                            Log.e("###", "Value ---> $p0")

                            updateRecentChatAfterDeleteMessage(
                                userId,
                                friendId,
                                currentMessage,
                                previousMessage
                            )
                            return Transaction.success(p0)
                        }

                        override fun onComplete(
                            p0: DatabaseError?,
                            p1: Boolean,
                            p2: DataSnapshot?
                        ) {
                            if (p0 != null) {
                                Log.e("###", "Firebase deleted by transaction failed.")
                            } else {
                                Log.e("###", "Firebase deleted by transaction succeeded.")
                            }
                        }
                    })
            }
        }

    }
*/
    fun clearPrivateChat(userId: String, friendId: String, success: (Long) -> Unit) {
        addClearChatTimestampInRecentNode(userId, friendId, success)
    }

    private fun addClearChatTimestampInRecentNode(
        p0: String,
        p1: String,
        success: (Long) -> Unit
    ) {
        val timestamp = System.currentTimeMillis()
        recentChatReference.child(p0)
            .child(p1)
            .child(KEY_CLEAR_CHAT_TIMESTAMP)
            .setValue(timestamp)
            .addOnSuccessListener {
                success(timestamp)
            }
    }

    fun blockUser(
        senderID: String,
        receiverID: String
    ) {
        recentChatReference.child(senderID).child(receiverID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(senderID).child(receiverID)
                            .child(KEY_BLOCKED_USER_BY)
                            .setValue(senderID)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        recentChatReference.child(senderID).child(receiverID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(senderID).child(receiverID)
                            .child(BLOCKED_USER)
                            .setValue(true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        recentChatReference.child(receiverID).child(senderID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(receiverID).child(senderID)
                            .child(BLOCKED_USER)
                            .setValue(true)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    fun unBlockUser(
        senderID: String,
        receiverID: String
    ) {
        recentChatReference.child(senderID).child(receiverID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(senderID).child(receiverID)
                            .child(KEY_BLOCKED_USER_BY)
                            .setValue(0)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        recentChatReference.child(senderID).child(receiverID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(senderID).child(receiverID)
                            .child(BLOCKED_USER)
                            .setValue(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        recentChatReference.child(receiverID).child(senderID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        recentChatReference.child(receiverID).child(senderID)
                            .child(BLOCKED_USER)
                            .setValue(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

}
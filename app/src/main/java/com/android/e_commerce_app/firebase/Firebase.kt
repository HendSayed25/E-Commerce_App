package com.android.e_commerce_app.firebase

import com.android.e_commerce_app.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getCollection(CollectionName: String): CollectionReference {

    val firestore = Firebase.firestore

    //create collection
    return firestore.collection(CollectionName)

}

//register
fun addUserToFirestore(
    user: User,
    onSuccessListener: OnSuccessListener<Void>,
    onFailureListener: OnFailureListener
) {

    val collection = getCollection(User.collectionName)

    //just document ,firebase that is enter id
    //document() , I am who entered the id
    val document = collection.document(user.id!!)

    //new document --> new object
    document.set(user).addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)


}
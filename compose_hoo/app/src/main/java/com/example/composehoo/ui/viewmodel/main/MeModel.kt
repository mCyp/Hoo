package com.example.composehoo.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import com.example.composehoo.db.repository.UserRepository

class MeModel(userRepo: UserRepository, id: Long): ViewModel() {
    val user = userRepo.findUserById(id)
}
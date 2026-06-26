/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sams.util;

import com.sams.model.User;

/**
 *
 * @author User
 */
public class SessionManager {

    private static User currentUser;

    private SessionManager() {
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && currentUser.isAdmin();
    }

    public static boolean isLecturer() {
        return isLoggedIn() && currentUser.isLecturer();
    }

    
    public static void clear() {
        currentUser = null;
    }
}


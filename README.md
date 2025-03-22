# 🛒 Grocery Cart

Grocery Cart is an Android application that allows users to manage their grocery shopping list. Users can add products, update quantities, and view their cart. The app uses Firebase for authentication and real-time database storage.

## 🌟 Features
- User authentication (login and registration)
- Add products to the cart
- Update product quantities
- View the cart with a list of products
- Logout functionality


## 🤖 Technologies Used

- Java
- XML
- Android Studio
- Material Design Components
- Firebase Authentication
- Firebase Realtime Database
- Android Studio

## 🛠️Setup
### Prerequisites
- Android Studio installed on your machine
- A Firebase project set up with Authentication and Realtime Database enabled

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/fannypil/groceryCart
   ```
2. Open the project in Android Studio.
3. Add your `google-services.json` file to the `app` directory.
4. Sync the project with Gradle files.

### Firebase Setup

1. Go to the Firebase Console.
2. Create a new project or use an existing one.
3. Enable Email/Password authentication in the Authentication section.
4. Set up the Realtime Database with the following rules:
    ```json
    {
      "rules": {
        ".read": "auth != null",
        ".write": "auth != null"
      }
    }
    ```

## 👨‍💻 Usage

1. Run the app on an emulator or a physical device.
2. Register a new user or log in with an existing account.
3. Add products to your cart and update quantities as needed.
4. View your cart and manage your products.

## 🔧 Project Structure

- `MainActivity.java`: Handles user authentication and main app logic.
- `AddProductFragment.java`: Fragment for adding products to the cart.
- `CartFragment.java`: Fragment for viewing and managing the cart.
- `LoginFragment.java`: Fragment for user login.
- `RegisterFragment.java`: Fragment for user registration.
- `CustomeAdapter.java`: Adapter for displaying products in the cart.
- `Product.java`: Model class for products.
- `User.java`: Model class for users.

## 📸 Demo
https://github.com/user-attachments/assets/a34e954a-1f20-4ee6-a58c-9ab51d840c13


## 💡 Acknowledgements
- This project was developed as part of an Development in android environment 1 coursework.
- [Material Design Components](https://material.io/develop/android/docs/getting-started)
- [Android Developers](https://developer.android.com/docs)

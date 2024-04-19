Title: Mobile Face Emotion Detection Application

Short description:
Developing a mobile application capable of detecting emotions from images captured by the real device camera. The application should analyze facial expressions and respond with a joke accordingly, for instance if the face expression is happy it will display a sad joke & the opposite .

Project Goals:
 Develop a user-friendly interface for the application.
 Implement camera functionality to capture images.
 Integrate machine learning algorithms for face expression detection.
 Use Java as the primary programming language for development.
 Optimize image loading and display using Glide library.

Display design:
There will be nice interface for welcoming and opening the application and button for directing to ask permission of using the camera and redirecting to the camera which will capture picture that picture will be forwarded for face detection recognition as it will be using machine learning algorithms as it will be either downloading the model and connect the path to it in case tensorflowLite or using the API in case of google Kit directly.

Chosen language: Java

Used technologies/APIS (with links):
Android Camera API:
https://developer.android.com/media/camera/camera-deprecated/camera-api
Face Expression Detection (either one of):
https://www.tensorflow.org/lite/api_docs/java/org/tensorflow/lite/package-summary
Or
https://developers.google.com/ml-kit/vision/face-detection
Image Optimization for UI [Glide]:
https://github.com/bumptech/glide

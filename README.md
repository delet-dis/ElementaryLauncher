# Elementary launcher

<img src="https://user-images.githubusercontent.com/47276603/122662091-93e03a00-d1ba-11eb-8c78-e4f07a589677.png" height = "220" align="right" hspace="50">


[![Android CI](https://github.com/delet-dis/ElementaryLauncher/actions/workflows/android.yml/badge.svg)](https://github.com/delet-dis/ElementaryLauncher/actions/workflows/android.yml)

[![codebeat badge](https://codebeat.co/badges/2f55c8e1-58ae-4e35-a454-27602b58e910)](https://codebeat.co/projects/github-com-delet-dis-elementarylauncher-main)
[![CodeFactor](https://www.codefactor.io/repository/github/delet-dis/elementarylauncher/badge)](https://www.codefactor.io/repository/github/delet-dis/elementarylauncher)

[<img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="auto" width="130"></a>](https://play.google.com/store/apps/details?id=com.delet_dis.elementarylauncher)

Elementary launcher is an [Android](https://en.wikipedia.org/wiki/Android_(operating_system)) app that helps people with vision problems interact with their home screen.

The goal of project is to make easy to access, use and customize launcher that replaces home screen.

Please check [CONTRIBUTING](CONTRIBUTING.md) page if you want to help.

## Project characteristics and tech-stack

<img src="https://user-images.githubusercontent.com/47276603/119392731-5511ae00-bcfa-11eb-942b-8314b2cc7928.jpg" width="336" align="right" hspace="20">

This project takes advantage of best practices, many popular libraries and tools in the Android ecosystem. 

* Tech-stack
    * [100% Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Flow](https://developer.android.com/kotlin/flow) - background-receiving data from database
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - navigation inside Activity
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify observers about database changes
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - event handling based on lifecycle
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) - store shortcuts data
        * [Android KTX](https://developer.android.com/kotlin/ktx) -  set of Kotlin extensions
        * [Palette](https://developer.android.com/training/material/palette-colors) -  colors selecting
        * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) -  using multiple screens inside activity
        * [ViewBinding](https://developer.android.com/topic/libraries/view-binding) -  getting links to interface elements
    * [Hilt](https://dagger.dev/hilt/) - dependency injection
    * [Firebase crashlytics](https://firebase.google.com/docs/crashlytics) - crash tracking
* Modern Architecture
    * Layers architecture
    * MVVM
* CI 
    * [GitHub Actions](https://github.com/features/actions)
    * Automatic code analyzing by 3rd party online tools
* UI
    * [Material design](https://material.io/design)

## Architecture
The entire application follows `layers architecture`.

It contains components that strictly fulfill their functions, as well as parts that are not part of them.
<img src="https://user-images.githubusercontent.com/47276603/125187599-95050400-e25a-11eb-9067-507007cc944a.png" width="700" hspace="5" vspace ="10">

## Data flows
The architecture of interaction between presentation and lower layers in both activities is similar.

All interaction occurs through the `ViewModel` of each `Activity` / `Fragment`, which then receives data from the repositories and notifies the view of the change.

Repositories, in turn, interact with the data services and broadcast receivers layer

### 🛬 OnboardingActivity
<img src="https://user-images.githubusercontent.com/47276603/122662600-6dbc9900-d1be-11eb-8063-7fb7b08e646c.png" width="600" hspace="5" vspace ="10">

> <img src="https://user-images.githubusercontent.com/47276603/122640649-e3772500-d12a-11eb-98ae-43fc95d000ba.png" width="30" hspace="5"> - request </br>
> <img src="https://user-images.githubusercontent.com/47276603/122640648-e2de8e80-d12a-11eb-869c-e20de1a1f773.png" width="30" hspace="5"> - response

### 📱 LauncherActivity

<img src="https://user-images.githubusercontent.com/47276603/122640641-df4b0780-d12a-11eb-98f7-4bf7137795f7.png" width="600" hspace="5" vspace ="10">

> <img src="https://user-images.githubusercontent.com/47276603/122640649-e3772500-d12a-11eb-98ae-43fc95d000ba.png" width="30" hspace="5"> - request </br>
> <img src="https://user-images.githubusercontent.com/47276603/122640648-e2de8e80-d12a-11eb-869c-e20de1a1f773.png" width="30" hspace="5"> - response
## More project screenshots
<img src="https://user-images.githubusercontent.com/47276603/119395044-2fd26f00-bcfd-11eb-86a5-1b051b98ca3d.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395047-306b0580-bcfd-11eb-881c-b3181a286f61.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395048-31039c00-bcfd-11eb-9688-afe7b3c71d86.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395051-31039c00-bcfd-11eb-9b41-fec92c8b39e6.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395053-319c3280-bcfd-11eb-823e-46d802a6410a.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395054-319c3280-bcfd-11eb-8c12-cdc442c89d7e.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395055-3234c900-bcfd-11eb-8dc9-2c9afaa1fd4b.jpg" width="200" hspace="5"  vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395057-32cd5f80-bcfd-11eb-9b8c-f1e04f25b9cc.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395059-32cd5f80-bcfd-11eb-9f94-2aee2f779a50.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395062-3365f600-bcfd-11eb-9ece-84d8a95be347.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395063-33fe8c80-bcfd-11eb-8016-ab6206b28834.jpg" width="200" hspace="5" vspace ="10">

## Contributors
[<img src="https://static.thenounproject.com/avatars/ranahpixel/resized/150/2a44fad30b5889a8d4c63cdad028997c.png" height="auto" width="50" style="border-radius:50%"></a>](https://thenounproject.com/ranahpixel/)
## License
```
MIT License

Copyright (c) 2021 Igor Efimov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

# 🎵 SonicLab - Neo-Analog Adaptive Music Player

**Status:** Phase 1 Complete ✅ | Active Development  
**Platform:** Android 8.0+ (API 26-34)  
**Architecture:** MVVM with Jetpack Compose  
**Repository:** https://github.com/Ozrachjoe85/SonicLab

---

## 🎯 Project Vision

**"Where sound becomes tangible"**

SonicLab is a professional-grade, open-source Android music player that combines the warmth of analog studio equipment with modern digital precision. Built with a **fully adaptive interface** that morphs seamlessly across any screen size—from phone to foldable to tablet to desktop.

### Core Philosophy
**"Information-dense but not cluttered. Atmospheric but not distracting. Pro-grade but not intimidating."**

---

## ✨ Current Features (Phase 1 - COMPLETE)

### ✅ Adaptive Layout System
- **One app, infinite surfaces** - No separate "modes"
- Automatically adapts to any screen size:
  - Compact (< 600dp): Standard phones, Z Fold cover
  - Medium (600-840dp): Z Fold inner portrait, small tablets
  - Wide (840-1200dp): Tablets, Z Fold landscape, DeX
  - Extra Wide (> 1200dp): External monitors, car displays
- Smooth transitions on fold/unfold, rotation
- State preservation across configuration changes

### ✅ Void Lab Theme System
**Default:** Neo-analog cyber-lab aesthetic
- Pure OLED black background (#000000) - battery efficient
- Electric cyan (#00F0FF) and magenta (#FF00AA) accents
- Holographic rim effects on album art
- Animated glows and effects
- 5 additional themes ready:
  - Neon Horizon (synthwave pink/orange)
  - Studio Mono (clinical grayscale)
  - Emerald Grid (matrix green)
  - Crimson Pulse (aggressive red)
  - Ice Circuit (cool blue minimal)

### ✅ Now Playing Screen
- Large centered album art with holographic rim
- Track title and artist with elegant typography
- Waveform-ready progress bar
- Real-time mini VU meters (L/R channels, 8 bars each)
- Large play/pause button with radial glow
- Quick actions: shuffle, repeat, favorite, more
- Fully scrollable (no cutoff in portrait)

### ✅ Bottom Navigation
- 4 main sections accessible via bottom nav bar
- Void Lab themed with cyan active indicators
- Smooth transitions between screens
- Always visible, never covers content

### ✅ Background Playback Service
- Foreground service architecture (won't be killed)
- Ready for Media3 ExoPlayer integration
- Notification controls (play/pause/next/previous/stop)
- Lock screen support prepared
- START_STICKY flag for auto-restart

### ✅ Placeholder Screens
- Collection (music library) - Coming in Phase 2
- Visuals (audio visualizations) - Coming in Phase 2
- Lab (EQ + spectrum analyzer) - Coming in Phase 2

---

## 🎨 Design Language: "Neo-Analog Cyber-Lab"

### Visual Identity
**Aesthetic:** 3 a.m. studio session in 2049 - mixing desk in dimly lit future workspace

**Color System:**
```
Background:     #000000 (Pure OLED black)
Surface:        #0A0A0F (Subtle panels)
Primary:        #00F0FF (Electric cyan)
Secondary:      #FF00AA (Magenta)
Accent:         #CC00FF (Purple)
Text Primary:   #F0F0FF (Warm white)
Text Secondary: #E0E0FF (Off-white)
VU Low:         #4CAF50 (Green)
VU Mid:         #FFB300 (Amber)
VU High:        #E53935 (Red)
```

**Typography:**
- Headings: Bold, geometric, letter-spaced (+1-2sp)
- Body: Clean Inter/Roboto for readability
- Technical: Monospace for data readouts

**Effects:**
- Holographic rim lighting on focal elements
- Radial glows on interactive buttons
- Glassmorphic panels (frosted, semi-transparent)
- Spring physics animations (60-120fps)

---

## 🏗️ Technical Architecture

### Tech Stack
```kotlin
Kotlin                  1.9.20
Android Gradle Plugin   8.2.0
Gradle                  8.6
Compose BOM             2023.10.01
Material 3              Latest
Hilt                    2.48
Media3 (planned)        1.2.0
Room (planned)          2.6.0
Coroutines              1.7.3
```

### Architecture Pattern
**MVVM (Model-View-ViewModel)**
- Model: Data layer (Room, repositories) - Phase 2
- View: Composable UI (current)
- ViewModel: State management (Phase 2)

### Key Components

#### 1. Adaptive Layout System
**File:** `AdaptiveLayout.kt`
```kotlin
sealed class ScreenSize {
    object Compact
    object Medium
    object Wide
    object ExtraWide
}

@Composable
fun AdaptiveLayout(
    compact: @Composable (ScreenConfig) -> Unit,
    medium: @Composable (ScreenConfig) -> Unit,
    wide: @Composable (ScreenConfig) -> Unit,
    extraWide: @Composable (ScreenConfig) -> Unit
)
```

#### 2. Theme System
**File:** `SonicTheme.kt`
```kotlin
enum class SonicTheme {
    VOID_LAB, NEON_HORIZON, STUDIO_MONO,
    EMERALD_GRID, CRIMSON_PULSE, ICE_CIRCUIT, CUSTOM
}

data class SonicColors(...)
data class ThemeEffects(
    glowRadius: Float,
    bloomStrength: Float,
    animationSpeed: Float,
    showHolographicRim: Boolean,
    ...
)
```

#### 3. Background Service
**File:** `MusicPlaybackService.kt`
```kotlin
class MusicPlaybackService : MediaSessionService() {
    // Foreground service for background playback
    // Lock screen controls
    // Notification management
}
```

#### 4. Navigation
**File:** `MainActivity.kt`
```kotlin
sealed class Screen {
    object NowPlaying
    object Collection
    object Visuals
    object Lab
}

@Composable
fun MainScreen() {
    Scaffold(bottomBar = { NavigationBar(...) }) {
        // Content with proper padding
    }
}
```

---

## 📂 Project Structure

```
SonicLab/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── AndroidManifest.xml
│   │       ├── java/com/soniclab/app/
│   │       │   ├── MainActivity.kt                    ✅ Complete
│   │       │   ├── SonicLabApp.kt                    ✅ Complete
│   │       │   ├── ui/
│   │       │   │   ├── adaptive/
│   │       │   │   │   └── AdaptiveLayout.kt         ✅ Complete
│   │       │   │   ├── screens/
│   │       │   │   │   ├── NowPlayingScreen.kt       ✅ Complete
│   │       │   │   │   └── PlaceholderScreen.kt      ✅ Complete
│   │       │   │   └── theme/
│   │       │   │       ├── SonicTheme.kt             ✅ Complete
│   │       │   │       ├── Color.kt                  ✅ Complete
│   │       │   │       └── Type.kt                   ✅ Complete
│   │       │   └── service/
│   │       │       └── MusicPlaybackService.kt       ✅ Complete
│   │       └── res/
│   │           ├── drawable/
│   │           │   ├── ic_play.xml                   ✅ Complete
│   │           │   ├── ic_pause.xml                  ✅ Complete
│   │           │   ├── ic_skip_previous.xml          ✅ Complete
│   │           │   ├── ic_skip_next.xml              ✅ Complete
│   │           │   └── ic_close.xml                  ✅ Complete
│   │           └── xml/
│   │               └── file_paths.xml                ✅ Complete
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 26-34
- Kotlin 1.9.20

### Build & Run
```bash
# Clone the repository
git clone https://github.com/Ozrachjoe85/SonicLab.git
cd SonicLab

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Run tests (when implemented)
./gradlew test
```

### Test on Different Screens
**Phone (Portrait):**
- Should show compact layout
- Bottom nav visible
- Content scrollable

**Phone (Landscape):**
- Should adapt to medium/wide layout
- More horizontal space utilized

**Foldable (Z Fold 6):**
- Cover screen: Compact layout
- Inner screen portrait: Medium layout
- Inner screen landscape: Wide layout
- Smooth fold/unfold transitions

**Tablet:**
- Wide/Extra Wide layouts
- Multi-panel possible in Phase 2

---

## 🎯 Roadmap

### ✅ Phase 1: Foundation (COMPLETE)
- [x] Adaptive layout system
- [x] Void Lab theme + 5 additional themes
- [x] Now Playing screen (scrollable)
- [x] Bottom navigation
- [x] Background playback service architecture
- [x] Placeholder screens for future sections

### 🔄 Phase 2: Core Playback (Next - 2-3 weeks)
- [ ] Integrate Media3 ExoPlayer
- [ ] Music file scanner (local storage)
- [ ] Room database for library
- [ ] Actual playback controls (working play/pause/skip)
- [ ] Queue management
- [ ] Test background playback (lock phone, music continues)
- [ ] Collection screen (browse songs/albums/artists)

### ⏳ Phase 3: Audio Engine (3-4 weeks)
- [ ] 32-band parametric EQ
- [ ] Real-time FFT analysis
- [ ] Working VU meters (connected to audio pipeline)
- [ ] Spectrum analyzer
- [ ] Gapless playback
- [ ] Crossfade between tracks
- [ ] Lab screen (EQ + spectrum controls)

### ⏳ Phase 4: Visualizations (2-3 weeks)
- [ ] 8 visualization engines:
  - Spectrum Cascade (waterfall)
  - Radial Pulse (circular burst)
  - Particle Storm (10K audio-reactive particles)
  - Lissajous (stereo phase curves)
  - VU Classic (giant analog needles)
  - Waveform Trace (oscilloscope)
  - Frequency Grid (3D bars)
  - Void Field (minimal glowing dots)
- [ ] Visuals screen (full-screen visualizations)

### ⏳ Phase 5: Intelligence (3-4 weeks)
- [ ] On-device AI mood detection
- [ ] BPM/key detection
- [ ] Genre classification
- [ ] Smart playlist generation
- [ ] Pattern learning (listening habits)

### ⏳ Phase 6: Advanced Layouts (2-3 weeks)
- [ ] Medium layout implementations
- [ ] Wide layout implementations
- [ ] Extra Wide dashboard (3-panel)
- [ ] DeX mode optimizations
- [ ] Keyboard shortcuts

### ⏳ Phase 7: Polish & Launch (2-3 weeks)
- [ ] Performance optimization
- [ ] Accessibility improvements (TalkBack, large text)
- [ ] Unit & UI tests
- [ ] Localization (multiple languages)
- [ ] Beta testing
- [ ] Play Store listing
- [ ] Launch! 🚀

**Estimated Total:** 15-20 weeks to v1.0

---

## 📱 Device Support

### Tested Devices
- ✅ Standard Android phones (any size)
- ✅ Samsung Galaxy Z Fold 6 (cover + inner screen)
- 🔄 Tablets (ready, not yet tested)
- 🔄 Samsung DeX (ready, not yet tested)

### Minimum Requirements
- Android 8.0 (Oreo, API 26)
- 2GB RAM
- 50MB storage

### Recommended
- Android 13+ for best experience
- OLED display (for true blacks)
- 4GB+ RAM
- Stereo speakers or good headphones

---

## 🎨 Design Decisions

### Why No "Car Mode"?
Instead of separate locked-down modes, we use **intelligent adaptive layouts**. The app automatically optimizes for any screen size. Car displays simply get the "Extra Wide" layout with appropriate touch targets and simplified interactions.

### Why OLED Black?
Pure black (#000000) background:
- ✅ Saves significant battery on OLED displays
- ✅ Creates dramatic contrast with neon accents
- ✅ Feels like a professional studio environment
- ✅ Reduces eye strain in dark environments

### Why Compose Over XML?
- ✅ Declarative UI is easier to reason about
- ✅ Better performance for animations
- ✅ Simpler adaptive layouts
- ✅ Modern Android best practice
- ✅ Less boilerplate code

### Why Foreground Service?
Android kills background services aggressively to save battery. A **foreground service** with notification:
- ✅ Keeps music playing when app minimized
- ✅ Keeps music playing when phone locked
- ✅ Provides lock screen controls
- ✅ Won't be killed by system

---

## 🐛 Known Issues

### Current Limitations
- ⚠️ No actual audio playback yet (Phase 2)
- ⚠️ VU meters are static placeholders (Phase 3)
- ⚠️ Album art is placeholder (Phase 2)
- ⚠️ Progress bar doesn't move (Phase 2)
- ⚠️ Buttons don't do anything yet (Phase 2)

### Planned Fixes
All of the above are intentional gaps that will be filled in Phase 2 and 3.

---

## 🤝 Contributing

### How to Contribute
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep functions under 50 lines when possible
- Use Compose best practices

### Areas Needing Help
- 🎵 Media3 ExoPlayer integration
- 🎨 Additional themes and color schemes
- 🌍 Translations (internationalization)
- 📱 Testing on different devices
- 🐛 Bug reports and fixes

---

## 📄 License

**TBD** - Will likely be MIT or Apache 2.0 (open source, permissive)

---

## 🙏 Acknowledgments

### Built With
- **Jetpack Compose** - Modern Android UI toolkit
- **Material Design 3** - Design system
- **Hilt** - Dependency injection
- **Kotlin Coroutines** - Async operations
- **Media3** - Audio playback (planned)

### Inspired By
- Vintage audio equipment (Neve, Studer, Marantz, McIntosh)
- Professional DAWs (Pro Tools, Ableton, FL Studio)
- Modern cyber aesthetics (Blade Runner 2049, Tron Legacy)
- 1960s-70s space age design

### Special Thanks
- Android developer community
- Jetpack Compose team at Google
- Open source contributors worldwide

---

## 📞 Contact & Support

**Developer:** Joe (Ozrachjoe85)  
**Repository:** https://github.com/Ozrachjoe85/SonicLab  
**Issues:** https://github.com/Ozrachjoe85/SonicLab/issues

### Support the Project
- ⭐ Star the repository
- 🐛 Report bugs
- 💡 Suggest features
- 🔀 Submit pull requests
- 📢 Share with others

---

## 📊 Project Stats

```
Phase 1 Status:        ✅ COMPLETE
Build Status:          ✅ PASSING
Lines of Code:         ~3,000 (well-documented)
Kotlin Files:          9
Compose Screens:       2 (+ 1 placeholder)
Themes:                6 presets + custom
Adaptive Breakpoints:  4
Test Coverage:         0% (Phase 6)
```

---

## 🎉 Changelog

### v0.1.0-alpha (Current - Phase 1 Complete)
**Released:** March 20, 2026

**Added:**
- ✅ Complete adaptive layout system
- ✅ Void Lab theme + 5 additional themes
- ✅ Now Playing screen with scrollable content
- ✅ Bottom navigation with 4 tabs
- ✅ Background playback service architecture
- ✅ Placeholder screens for future features
- ✅ VU meter UI (static for now)
- ✅ All permissions configured
- ✅ Notification icons
- ✅ FileProvider for sharing

**Fixed:**
- ✅ Screen cutoff in portrait mode
- ✅ Missing bottom navigation
- ✅ Content overlapping with UI elements

**Known Issues:**
- ⚠️ No actual playback yet (Phase 2)
- ⚠️ Placeholder content only

---

**Next Update:** Phase 2 - Media3 Integration & Actual Playback

---

*Built with ❤️ and lots of ☕ by Joe | Powered by Kotlin & Jetpack Compose*

**"Where sound becomes tangible"** 🎵✨

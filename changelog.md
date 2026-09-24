# CHANGELOG

## v1.0.1
- Fix "error parsing time stamps" when loading routines with TEXT-stored dates
- Use routinexe-name.png image as app title instead of text label
- Fix exercise pagination not showing page 1 on initial load
- Create DB next to AppImage file (use APPIMAGE env var)
- Find seed.sql inside AppImage mount (APPDIR env var)
- Bundle SQLite JDBC in fat JAR via Maven Shade Plugin
- CI: Cross-platform release workflow (Windows EXE, macOS DMG)
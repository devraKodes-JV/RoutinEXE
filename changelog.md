# CHANGELOG

## v1.0.1
- Fix "error parsing time stamps" when loading routines with TEXT-stored dates
- Embed seed.sql in JAR for reliable seed loading in AppImage
- Create DB next to AppImage file (use `APPIMAGE` env var)
- Fix exercise list showing empty — pagination refresh after data load
- Use routinexe-name.png image as app title instead of text label
- Fix exercise pagination not showing page 1 on initial load
- Bundle SQLite JDBC in fat JAR via Maven Shade Plugin
- CI: Cross-platform release workflow (Windows EXE, macOS DMG)
Just download the font if you want it.

See how you can use it in `cursed.html`.

---

This directory contains the data needed to create a custom font with the Rail Clock seconds animated, using ligatures in FontForge.

## Custom font generation steps

1. Launch FontForge, create a new font
2. Encoding > Reencode > Custom
3. Encoding > Remove unused slots
4. Edit and copy the script from `font-forge-script.py` (updating the path to the generated SVGs as needed)
5. In FontForge, run `File > Execute Script...` and paste the script in, then press OK
6. Element > Merge Font and select the ra_numerics.ttf file, answering Yes to keep the kerning
7. Select the first clock glyph, then Shift+PgDn until all clock glyphs are selected
8. Metrics > Center in Width (this takes a while)
9. Element > Add Extrema
10. File > Generate Fonts... and select TrueType as the output format, saving as railclock-ligatures.ttf

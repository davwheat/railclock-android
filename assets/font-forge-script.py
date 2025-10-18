import fontforge

font = fontforge.activeFont()
#font.removeLookup('ligatures')
font.addLookup('ligatures','gsub_ligature',None,(("liga",(("latn",("dflt")),)),))
subtable = 0
font.addLookupSubtable('ligatures', 'ligaturesanim')

lookup = {
    '0': 'zero',
    '1': 'one',
    '2': 'two',
    '3': 'three',
    '4': 'four',
    '5': 'five',
    '6': 'six',
    '7': 'seven',
    '8': 'eight',
    '9': 'nine',
}

font.createChar(ord("0"), 'zero')
font.createChar(ord("1"), 'one')
font.createChar(ord("2"), 'two')
font.createChar(ord("3"), 'three')
font.createChar(ord("4"), 'four')
font.createChar(ord("5"), 'five')
font.createChar(ord("6"), 'six')
font.createChar(ord("7"), 'seven')
font.createChar(ord("8"), 'eight')
font.createChar(ord("9"), 'nine')

for i in range(0, 60):
    char = font.createChar(-1, "anim_" + str(i))
    string = str(i).zfill(2)
    tup = tuple([lookup[x] for x in string])
    glyph = char.importOutlines("/Users/david/Git/railclock-wearos/assets/out/"+string+"000.svg")
    glyph.addPosSub('ligaturesanim', tup)
    glyph.addExtrema()
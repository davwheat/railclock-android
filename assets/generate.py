import os

output = "out/"

with open('template.svg', 'r') as f:
    SVG_TEMPLATE = f.read()

def generate_svg(milliseconds: int):
    # Calculate rotation angles
    angle1 = -(((milliseconds) / 60000 * 360) - 180)  # Full rotation every minute
    angle2 = -angle1  # Opposite rotation

    # Replace placeholders in the SVG template
    svg_content = SVG_TEMPLATE.replace('$$1$$', str(angle1)).replace('$$2$$', str(angle2))

    # Write the generated SVG to the output path
    with open(os.path.join(output, f"{milliseconds:05d}.svg"), 'w') as f:
        f.write(svg_content)

for ms in range(0, 60):
    generate_svg(ms*1000)

print("SVG generation complete.")
print("Please optimise with SVGO before importing to FontForge: $ npx svgo --folder out/")
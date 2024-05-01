# we need to preprocess the SVGs to be roughly the same, so that the model doesnt need to learn as much

# - remove all comments (if ommentss were meaningfull i would leave them, but they are mostly just "this is taken from font awsome"...we dont need this)
# - remove classes. Classes are also mostly useless branding
# - we should be able to rescale them all to the same viewbox. think of it as some for of normalizing output data.

import re
import glob
import os
from resize_svg import resize_svg


# decimal_matcher = re.compile(r"-*\d+\.?\d*")
# path_matcher = re.compile(r"-*\d+\.?\d*")
# viewbox_match = re.compile(r"viewBox=\"(.*?)\"")

SVG_W = 256
SVG_H = 256


def normalize_svg_viewbox(svg_string: str, new_w: int, new_h: int) -> str:
    # parse existing viewbox
    return resize_svg(svg_string, new_w, new_h).decode("utf-8")


comment_matcher = re.compile(r"<!--.*-->")
class_matcher = re.compile(r"class=\".+\"")


def clean_svg(svg_string: str) -> str:
    no_comments = re.sub(comment_matcher, "", svg_string)
    no_classes = re.sub(class_matcher, "", no_comments)
    return no_classes


pth = "merged_icons"
out = "clean_icons"
for file in glob.glob(os.path.join(pth, "*.svg")):
    name = os.path.split(file)[-1].replace(".svg", "")
    try:
        with open(file, "r") as f:
            data = f.read()
            rescaled = normalize_svg_viewbox(data, SVG_W, SVG_H)
            cleaned = clean_svg(rescaled)
            print(name)

        with open(os.path.join(out, f"{name}_clean.svg"), "w") as out_file:
            out_file.write(cleaned)
    except:
        print(f"ERROR:  {name}")

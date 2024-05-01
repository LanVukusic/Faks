"""
Resize a SVG file to a new size. Coordinates of paths and gradient definitions get transposed to corresponding
values in the new canvas. Everything else remain unchanged.
"""

import re
from argparse import ArgumentParser
from xml.etree import ElementTree

_SVG_NAMESPACE = "http://www.w3.org/2000/svg"
_VALID_PATH_COMMANDS = "MLTHVCSQAZ"
_RE_FLOAT = re.compile(r"[+-]?\d*(\.\d+)?")


def resize_path(path, x_factor, y_factor):
    result = ""
    index = 0
    length = len(path)

    def eat_number(factor):
        nonlocal result
        nonlocal index
        match = _RE_FLOAT.match(path[index:])
        if not match:
            return
        found = match.group(0)
        scaled = factor * float(found)
        index += len(found)
        result += f"{scaled:.4f}".rstrip("0").rstrip(".")

    def skip_space():
        nonlocal index
        while path[index] == " " or path[index] == ",":
            index += 1

    def eat_space():
        nonlocal result
        skip_space()
        result += " "

    def eat_scale_xy():
        nonlocal result
        eat_number(x_factor)
        skip_space()
        result += ","
        eat_number(y_factor)

    def eat_for_command(command):
        if command in "MLT":
            eat_scale_xy()
        elif command == "H":
            eat_number(x_factor)
        elif command == "V":
            eat_number(y_factor)
        elif command == "C":
            eat_scale_xy()
            eat_space()
            eat_scale_xy()
            eat_space()
            eat_scale_xy()
        elif command in "SQ":
            eat_scale_xy()
            eat_space()
            eat_scale_xy()
        elif command == "A":
            eat_scale_xy()
            eat_space()
            eat_number(1)  # x-axis-rotation
            eat_space()
            eat_number(1)  # large-arc-flag
            eat_space()
            eat_number(1)  # sweep-flag
            eat_space()
            eat_scale_xy()
        elif command == "Z":
            pass
        else:
            raise ValueError("Unknown command", command)

    repeating_command = ""
    while index < length:
        skip_space()
        lead = path[index].upper()
        if lead in _VALID_PATH_COMMANDS:
            result += path[index]
            index += 1
            eat_for_command(lead)
            repeating_command = lead
        else:
            result += " "
            eat_for_command(repeating_command)

    return result


def _resize_element_path(el, x_factor, y_factor):
    path = el.get("d")
    el.set("d", resize_path(path, x_factor, y_factor))


def _resize_element_svg(el, width, height):
    assert type(width) == str
    assert type(height) == str
    el.set("width", width)
    el.set("height", height)
    el.set("viewBox", f"0 0 {width} {height}")


def _resize_element_gradient(el, x_factor, y_factor):
    for attr in ["x1", "y1", "x2", "y2"]:
        value = el.get(attr)
        if value:
            factor = x_factor if attr.startswith("x") else y_factor
            new_value = float(value) * factor
            el.set(attr, str(new_value))


def resize_svg(source, width, height):
    "Resize source svg to `width` and `height`"
    ElementTree.register_namespace("", _SVG_NAMESPACE)
    x_factor = 1
    y_factor = 1
    root = ElementTree.fromstring(source)
    for element in root.iter():
        if element.tag.endswith("svg"):
            viewbox = element.get("viewBox").split(" ")
            _resize_element_svg(element, str(width), str(height))
            x_factor = 1.0 / float(viewbox[2]) * float(width)
            y_factor = 1.0 / float(viewbox[3]) * float(height)
        elif element.tag.endswith("Gradient"):  # (linear|radial)Gradient
            _resize_element_gradient(element, x_factor, y_factor)
        elif element.tag.endswith("path"):
            _resize_element_path(element, x_factor, y_factor)
    return ElementTree.tostring(root)

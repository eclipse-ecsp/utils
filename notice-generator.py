import xml.etree.ElementTree as ET
import os


def parse_components(xml_file):
    print("Parsing components")
    tree = ET.parse(xml_file)
    root = tree.getroot()
    comps = []
    namespace = "{" + root.tag.split("}")[0][1:] + "}"
    components = root.findall(f".//{namespace}component")

    for component in components:
        name = component.find(f".//{namespace}name")
        try:
            version = component.find(f".//{namespace}version")
            license = component.find(f".//{namespace}license")
            licenseid = license.find(f".//{namespace}id")

            comps.append({
                'name': name.text,
                'version': version.text,
                'license': licenseid.text,
            })
        except:
            pass
    return comps


def format_output(component_array):
    string = ''
    for comp in component_array:
        string += comp.get('name') + '(' + comp.get('version') + ')' + '\n'
        string += '\t' + 'LICENSE' + ':' + comp.get('license') + '\n'
        string += '\n'
    return string


files = [f for f in os.listdir('.')]
print(files)
# Change this to the path of your XML file
xml_file = "./bom.xml"

component_array = parse_components(xml_file)
output = format_output(component_array)

# Define the placeholder to be replaced
placeholder = "{{third_party_content}}"

# Read the contents of the file
with open("./NOTICE.template.md", "r") as file:
    content = file.read()

# Replace the placeholder with the components string
modified_content = content.replace(placeholder, output)

# Write the modified content back to the file
with open("NOTICE.md", "w+") as file:
    file.write(modified_content)

print("Notice generated successfully!")

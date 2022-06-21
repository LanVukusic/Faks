from bs4 import BeautifulSoup
import requests
import os

base_url = "https://teaching.lavbic.net/TPO/2021-2022/"
main_url = "https://teaching.lavbic.net/TPO/2021-2022/Uvod.html"

username = "tpo"
password = "gobbledygook"

pages_to_scrape = [
    # "https://teaching.lavbic.net/TPO/2021-2022/Uvod.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/Projektno-vodenje.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/Procesi-izdelave.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/Agilni-razvoj.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/Zajem-zahtev.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/V3.html",
    # "https://teaching.lavbic.net/TPO/2021-2022/V4.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Nacrtovanje-sistema.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Zasnova-arhitekture.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Implementacija.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Testiranje.html",
    "https://teaching.lavbic.net/TPO/2021-2022/CICD.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Evolucija.html",
    "https://teaching.lavbic.net/TPO/2021-2022/Varnostno-inzenirstvo.html",
]

# get main page
# requests set content-type to text/html
r = requests.get(
    main_url,
    auth=(username, password),
    headers={
        "Content-Type": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
        "accept-language": "en-US,en;q=0.9,sl-SI;q=0.8,sl;q=0.7,hr;q=0.6",
        "user-agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.75 Safari/537.36",
    },
)
main_page = BeautifulSoup(
    r.text,
    "html.parser",
)


# remove component from main page
main_page.find("div", {"class": "book-summary"}).decompose()
main_page.find("div", {"class": "book-header"}).decompose()
main_page.find("div", {"class": "page-wrapper"}).decompose()

# two of those
main_page.find("a", {"class": "navigation"}).decompose()
main_page.find("a", {"class": "navigation"}).decompose()

# remove css rule for padding left
original_url = []
new_url = []

# loop over pages to scrape
for page in pages_to_scrape:

    # get page
    page_content = BeautifulSoup(
        requests.get(page, auth=(username, password)).text, "html.parser"
    )
    page_content.find("div", {"class": "book-header"}).decompose()
    page_content.find("div", {"id": "google_translate_element"}).decompose()
    page_content.find("div", {"id": "feedback-obrazec"}).decompose()
    try:
        page_content.find("table", {"class": "dodatnoGradivo"}).decompose()
    except:
        ...
    # get all styles from page_content
    styles = page_content.find_all("link", {"rel": "stylesheet"})
    # # download styles
    for style in styles:
        # download style from style url
        print(style)
        style_url = style.get("href")
        # download style
        style_content = requests.get(
            f"{base_url}/{style_url}", auth=(username, password)
        ).text
        # write style to file
        os.makedirs(os.path.dirname(style_url), exist_ok=True)
        with open(style_url, "w") as f:
            f.write(style_content)

    # get all images from page_content
    images = page_content.find_all("img")

    # download images
    for image in images:
        # download image from image url
        image_url = image.get("src")
        # download image
        image_content = requests.get(
            f"{base_url}/{image_url}", auth=(username, password)
        ).content

        # if we have a long ass lavbic svg url, we need to fix it
        if len(image_url) > 100:
            # shorten name
            image_content = requests.get(image_url, auth=(username, password)).content
            # hash from string
            image_name = str(abs(hash(image_url)))
            save_path = f"assets/svg/{image_name}.svg"

            original_url.append(image_url)
            new_url.append(save_path)

        else:
            save_path = image_url

        # modify image url

        # write image to file
        try:
            os.makedirs(os.path.dirname(save_path), exist_ok=True)
            with open(save_path, "wb") as f:
                f.write(image_content)
        except:
            print(f"Could not write {save_path}")

    # append el to main page
    el = page_content.find("div", {"class": "page-wrapper"})
    main_page.find("div", {"class": "body-inner"}).append(el)

# remove scripts from soup
for script in main_page.find_all("script"):
    if (
        "https://mathjax.rstudio.com/latest/MathJax.js?config=TeX-MML-AM_CHTML"
        in script.prettify()
    ):
        continue
    script.decompose()

# export main apge to html

page = main_page.prettify()

for i in range(len(original_url)):
    page = page.replace(original_url[i], new_url[i])

with open("main.html", "w", encoding="utf-8") as f:
    f.write(
        page.replace("lavbic", "totalnonelavbic")
        .replace("Ä", "č")
        .replace("Å¡", "š")
        .replace("Å¾", "ž")
        .replace("Å ", "Š")
        .replace("Ä", "Č")
        .replace("â ", "")
        .replace("â", "-")
        .replace("Ã© ", "e")
    )

with open("assets/style/stil.css", "wa") as stil:
    fix = """.book-body{
	    left: 0 !important;
    }   
    """
    stil.write(fix)

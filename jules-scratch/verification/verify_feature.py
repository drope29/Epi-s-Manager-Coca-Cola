from playwright.sync_api import sync_playwright

def run(playwright):
    browser = playwright.chromium.launch(headless=True)
    context = browser.new_context()
    page = context.new_page()

    # Go to the page
    page.goto("http://localhost:5173/colaboradores")

    # Wait for the search input to be visible
    page.wait_for_selector('input[placeholder="Procurar por nome, RE, cargo..."]')

    # Click the "Adicionar" button
    page.get_by_role("button", name="Adicionar").click()

    # Fill out the form
    page.fill('input[placeholder="Nome"]', "Jules")
    page.fill('input[placeholder="Sobrenome"]', "da Silva")
    page.locator('select').first.select_option("ANTONIO CARLOS")
    page.fill('input[placeholder="RE"]', "12345")
    page.fill('input[placeholder="Cargo"]', "Engenheiro de Software")
    page.locator('select').last.select_option("MANHA")
    page.check('input[value="homem"]')

    # Take a screenshot
    page.screenshot(path="jules-scratch/verification/verification.png")

    browser.close()

with sync_playwright() as playwright:
    run(playwright)
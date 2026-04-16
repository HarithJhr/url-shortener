const form = document.getElementById("urlForm")
const longUrlInput = document.getElementById("longUrl")
const messageDiv = document.getElementById("message")
const resultDiv = document.getElementById("result")
const shortUrlAnchor = document.getElementById("shortenedUrl")

form.addEventListener("submit", async (e) => {
    e.preventDefault()

    const longUrl = longUrlInput.value.trim()

    messageDiv.textContent = ""
    resultDiv.classList.add("hidden")

    try {
        const response = await fetch(`/api/urls`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ longUrl: longUrl })
        })

        if (!response.ok) {
            throw new Error("Failed to shorten URL")
        }

        const data = await response.json()

        shortUrlAnchor.textContent = data.shortUrl
        shortUrlAnchor.href = data.shortUrl
        resultDiv.classList.remove("hidden")
    } catch (error) {
        messageDiv.textContent = error.message
    }
})
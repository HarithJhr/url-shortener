async function loadProfile() {
    try {
        const response = await fetch("/api/me")

        if (!response.ok) {
            throw new Error("Not logged in")
        }

        const user = await response.json()

        document.getElementById("profileSection").style.display = "block"

        document.getElementById("name").textContent = user.name
        document.getElementById("email").textContent = user.email
        document.getElementById("profilePic").src = user.pictureUrl

    } catch (e) {
        document.getElementById("errorSection").style.display = "block"
    }
}

loadProfile()
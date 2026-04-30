async function loadUser() {
    try {
        const response = await fetch("/api/me");

        if (!response.ok) {
            throw new Error("Not logged in");
        }

        const user = await response.json();

        // hide login section
        document.getElementById("authSection").style.display = "none";

        // show user section
        const userSection = document.getElementById("userSection");
        userSection.style.display = "block";

        document.getElementById("welcomeText").textContent = `Welcome, ${user.name}`;

    } catch (e) {
        // not logged in
        // show login button
        document.getElementById("authSection").style.display = "block";
        document.getElementById("userSection").style.display = "none";
    }
}

loadUser();
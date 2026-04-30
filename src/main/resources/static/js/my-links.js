async function loadLinks() {
    try {
        const response = await fetch("/api/my-links");

        if (!response.ok) {
            throw new Error("Not logged in");
        }

        const links = await response.json();

        const container = document.getElementById("linksContainer");

        if (links.length === 0) {
            container.innerHTML = "<p>No links yet.</p>";
            return;
        }

        links.forEach(link => {
            const div = document.createElement("div");

            div.innerHTML = `
                <p><strong>Short:</strong> ${link.shortCode}</p>
                <p><strong>Original:</strong> ${link.longUrl}</p>
                <p><strong>Clicks:</strong> ${link.clickCount}</p>
                <hr/>
            `;

            container.appendChild(div);
        });

    } catch (err) {
        document.getElementById("linksContainer").innerHTML =
            "<p>You are not logged in.</p>";
    }
}

loadLinks();
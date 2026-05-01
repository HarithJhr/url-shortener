async function loadLinks() {
    try {
        const response = await fetch("/api/my-links");

        if (!response.ok) {
            throw new Error("Not logged in");
        }

        const links = await response.json();

        const linksContainer = document.getElementById("linksContainer");

        linksContainer.innerHTML = ""

        if (links.length === 0) {
            linksContainer.innerHTML = "<p>No links yet.</p>";
            return;
        }

        links.forEach(link => {
            const div = document.createElement("div");

            div.innerHTML = `
                <p><strong>Short:</strong> ${link.shortCode}</p>
                <p><strong>Original:</strong> ${link.longUrl}</p>
                <p><strong>Clicks:</strong> ${link.clickCount}</p>
                <button class="delete-button" data-id="${link.id}">Delete</button>
                <hr/>
            `;

            linksContainer.appendChild(div);
        });

        document.querySelectorAll(".delete-button").forEach(button => {
            button.addEventListener("click", async () => {
                const id = button.dataset.id
                const confirmed = confirm("Delete this shortened URL?")

                if (confirmed) {
                    await deleteUrl(id)
                }
            })
        })

    } catch (err) {
        document.getElementById("linksContainer").innerHTML =
            "<p>You are not logged in.</p>";
    }
}


async function deleteUrl(id) {
    try {
        const csrf = await fetchCsrfToken();

        const response = await fetch(`/api/urls/${id}`, {
            method: "DELETE",
            headers: {
                [csrf.headerName]: csrf.token
            }
        })

        if (!response.ok) {
            throw new Error("Failed to delete URL")
        }

        await loadLinks()
    } catch (e) {
        console.error(e);
        alert("Failed to delete URL")
    }
}




loadLinks();
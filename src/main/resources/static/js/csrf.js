async function fetchCsrfToken() {
    const response = await fetch("/api/csrf");

    if (!response.ok) {
        throw new Error("Failed to fetch CSRF token");
    }

    return await response.json();
}
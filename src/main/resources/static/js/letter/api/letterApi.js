export async function getLatestLetter() {

    const url = "/api/v1/letter/latest"
    
    return await fetch(url).then(response => {
            if (response.ok) {
                return response.json();
            }
            return response.text().then(text => {
                throw new Error(text)
            });
        }
    );
}
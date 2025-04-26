const openSpotifyLoginPopup = (url: string): Promise<string | null> => {
    return new Promise((resolve) => {
        const width = 600;
        const height = 600;
        const left = window.screen.width / 2 - width / 2;
        const top = window.screen.height / 2 - height / 2;

        const popup = window.open(url, "Spotify Login", `width=${width},height=${height},left=${left},top=${top}`);

        if (!popup) {
            resolve(null);
            return;
        }

        const checkPopupClosed = setInterval(() => {
            if (popup.closed) {
                clearInterval(checkPopupClosed);
                resolve(localStorage.getItem("userId"));
            }
        }, 1000);

        window.addEventListener("message", (event) => {
            if (event.data?.userId) {
                clearInterval(checkPopupClosed);
                popup.close();
                resolve(event.data.userId);
            }
        });

        setTimeout(() => {
            if (!popup.closed) popup.close();
            clearInterval(checkPopupClosed);
            resolve(null);
        }, 300000);
    });
};

export default openSpotifyLoginPopup;

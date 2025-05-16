import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import ProfilePage from "./pages/ProfilePage";
import SpotifyProfile from "./pages/SpotifyProfile";
import "./styles/global.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Lineup from "./pages/LineupPage.tsx";
import FestivalsPage from "./pages/FestivalsPage.tsx";
import { ConcertSortingFilteringProvider } from "./contexts/ConcertSortingFiltering.tsx";
import SpotifyAuthSuccess from "./components/spotify/SpotifyAuthSuccess.tsx";
import LandingPage from "./pages/LandingPage.tsx";
import {LineupSortingFilteringProvider} from "@/contexts/LineupSortingFilteringContext.tsx";

const router = createBrowserRouter([
  {
    path: "",
    element: <App />,
    children: [
      {
        index: true,
        element: <LandingPage />,
      },
      {
        path: "home",
        element: <LandingPage />,
      },
      {
        path: "festivals",
        element: <FestivalsPage />,
      },
      {
        path: "profile",
        element: <ProfilePage />,
        children: [
          {
            index: true,
            element: <SpotifyProfile />,
          },
        ],
      },
      {
        path: "lineup",
        element: <Lineup />,
      },
      { path: "spotify-auth-success", element: <SpotifyAuthSuccess /> },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <LineupSortingFilteringProvider>
      <ConcertSortingFilteringProvider>
            <RouterProvider router={router} />
      </ConcertSortingFilteringProvider>
    </LineupSortingFilteringProvider>
  </React.StrictMode>
);

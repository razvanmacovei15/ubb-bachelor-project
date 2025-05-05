import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import ProfilePage from './pages/ProfilePage';
import SpotifyProfile from './pages/SpotifyProfile';
import './index.css';
import "./styles/global.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import HomePage from "./pages/HomePage.tsx";
import Lineup from "./pages/LineupPage.tsx";
import { ConcertProvider } from "./contexts/ConcertContext.tsx";
import { LineupProvider } from "./contexts/LineupContext.tsx";
import ConcertsPage from "./pages/ConcertsPage.tsx";
import StatisticsPage from "./pages/StatisticsPage.tsx";
import { ConcertSortingFilteringProvider } from "./contexts/ConcertSortingFiltering.tsx";
import SpotifyAuthSuccess from "./components/spotify/SpotifyAuthSuccess.tsx";

const router = createBrowserRouter([
  {
    path: "",
    element: <App />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "concerts",
        element: <ConcertsPage />,
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
      {
        path: "statistics",
        element: <StatisticsPage />,
      },
      { path: "spotify-auth-success", element: <SpotifyAuthSuccess /> },

    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ConcertSortingFilteringProvider>
      <ConcertProvider>
        <LineupProvider>
          <RouterProvider router={router} />
        </LineupProvider>
      </ConcertProvider>
    </ConcertSortingFilteringProvider>
  </React.StrictMode>
);

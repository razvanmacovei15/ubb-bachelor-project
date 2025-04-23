import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import "./styles/global.css";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import App from "./App.tsx";
import HomePage from "./pages/HomePage.tsx";
import Profile from "./pages/ProfilePage.tsx";
import Lineup from "./pages/LineupPage.tsx";
import { ConcertProvider } from "./contexts/ConcertContext.tsx";
import { LineupProvider } from "./contexts/LineupContext.tsx";
import ConcertsPage from "./pages/ConcertsPage.tsx";
import StatisticsPage from "./pages/StatisticsPage.tsx";
import { ConcertSortingFilteringProvider } from "./contexts/ConcertSortingFiltering.tsx";

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
        element: <Profile />,
      },
      {
        path: "lineup",
        element: <Lineup />,
      },
      {
        path: "statistics",
        element: <StatisticsPage />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ConcertSortingFilteringProvider>
      <ConcertProvider>
        <LineupProvider>
          <RouterProvider router={router} />
        </LineupProvider>
      </ConcertProvider>
    </ConcertSortingFilteringProvider>
  </StrictMode>
);

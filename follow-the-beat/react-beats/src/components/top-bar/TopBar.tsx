import { useNavigate, useLocation } from "react-router-dom";
import TopBarButton from "./TopBarButton";
import "./TopBar.css";

const TopBar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className="top-bar">
      <div className="logo-container">
        <h1>Follow The Beat</h1>
      </div>
      <div className="menu-container">
        <TopBarButton
          title="Home"
          onClick={() => {
            navigate("/");
          }}
          isActive={location.pathname === "/"}
        />
        <TopBarButton
          title="Festivals"
          onClick={() => {
            navigate("/festivals");
          }}
          isActive={location.pathname === "/festivals"}
        />
        <TopBarButton
          title="My Lineup"
          onClick={() => {
            navigate("/lineup");
          }}
          isActive={location.pathname === "/lineup"}
        />
        <TopBarButton
          title="Profile"
          onClick={() => {
            navigate("/profile");
          }}
          isActive={location.pathname === "/profile"}
        />
      </div>
    </div>
  );
};

export default TopBar;

import { useNavigate } from "react-router-dom";
import TopBarButton from "./TopBarButton";
import "./TopBar.css";

const TopBar = () => {
  const navigate = useNavigate();

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
        />
        <TopBarButton
          title="Festivals"
          onClick={() => {
            navigate("/festivals");
          }}
        />
        <TopBarButton
          title="My Lineup"
          onClick={() => {
            navigate("/lineup");
          }}
        />
        <TopBarButton
          title="Profile"
          onClick={() => {
            navigate("/profile");
          }}
        />
      </div>
    </div>
  );
};

export default TopBar;

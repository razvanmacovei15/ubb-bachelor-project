import { Outlet } from "react-router-dom";
import "./App.css";
import "./index.css";
import "../src/styles/global.css";

function App() {
  return (
    <div className="w-full h-screen flex flex-col">
      <Outlet />
    </div>
  );
}

export default App;

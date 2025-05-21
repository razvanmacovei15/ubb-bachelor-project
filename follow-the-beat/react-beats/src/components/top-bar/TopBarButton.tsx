import "./TopBarButton.css";

type TopBarButtonProps = {
  onClick: () => void;
  title: string;
  isActive?: boolean;
};

const TopBarButton: React.FC<TopBarButtonProps> = ({
  onClick,
  title,
  isActive = false,
}: TopBarButtonProps) => {
  return (
    <button
      className={`button-class ${isActive ? "active" : ""}`}
      onClick={onClick}
    >
      <h1>{title}</h1>
    </button>
  );
};

export default TopBarButton;

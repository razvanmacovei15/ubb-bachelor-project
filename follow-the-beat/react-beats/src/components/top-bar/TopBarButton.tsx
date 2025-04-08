import "./TopBarButton.css";

type TopBarButtonProps = {
  onClick: () => void;
  title: string;
};

const TopBarButton: React.FC<TopBarButtonProps> = ({
  onClick,
  title,
}: TopBarButtonProps) => {
  return (
    <button className="button-class" onClick={onClick}>
      <h1>{title}</h1>
    </button>
  );
};

export default TopBarButton;

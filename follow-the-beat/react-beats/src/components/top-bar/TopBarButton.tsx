type TopBarButtonProps = {
    onClick: () => void;
    title: string;
}

const TopBarButton: React.FC<TopBarButtonProps> = ({  onClick, title } : TopBarButtonProps) => {
    return (
        <button className="bg-amber-300 p-2 rounded-2xl h-10 w-[100px]" onClick={onClick}>
            <h1 className="align-middle items-center justify-center text-center w-full">{title}</h1>
        </button>
    );
}

export default TopBarButton;
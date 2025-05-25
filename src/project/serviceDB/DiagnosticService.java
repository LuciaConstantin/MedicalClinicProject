package project.serviceDB;

import project.ClinicDAO;
import project.config.ConnectionProvider;
import project.exceptions.DiagnosticException;
import project.models.Diagnostic;
import project.repository.DiagnosticRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DiagnosticService  extends ClinicDAO<Diagnostic> {
    private final DiagnosticRepository diagnosticRepository = DiagnosticRepository.getInstance();

    public DiagnosticService() {}

    @Override
    public void create(Diagnostic diagnostic) {
        try (Connection connection = ConnectionProvider.getConnection()) {
            diagnosticRepository.insertDiagnostic(connection, diagnostic);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Diagnostic getById(long id) {
        Optional<Diagnostic> client = diagnosticRepository.getDiagnosticById(ConnectionProvider.getConnection(), id);
        return client.orElseThrow(DiagnosticException::new);
    }


}
